import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import editPage from '@/views/Admin/Kelas/editPage.vue'
import api from '@/lib/axios'

vi.mock('@/lib/axios')
vi.mock('vue-toastification', () => ({
  useToast: () => ({ error: vi.fn(), success: vi.fn() }),
}))

const router = createRouter({
  history: createMemoryHistory(),
  routes: [
    { path: '/admin/kelas/edit/:id', name: 'admin.kelas.edit', component: editPage },
    { path: '/admin/kelas', name: 'admin.kelas.index', component: { template: '<div/>' } },
  ],
})

describe('Kelas editPage', () => {
  beforeEach(async () => {
    vi.clearAllMocks()
    vi.mocked(api.get).mockResolvedValue({
      data: { data: { nama_kelas: 'Yoga', tarif: 50000 } },
    })
    await router.push('/admin/kelas/edit/1')
    await router.isReady()
  })

  it('shows loading spinner while fetching', () => {
    vi.mocked(api.get).mockReturnValue(new Promise(() => {}))
    const wrapper = mount(editPage, { global: { plugins: [router] } })
    expect(wrapper.find('.v-progress-circular').exists()).toBe(true)
  })

  it('pre-fills form with fetched data', async () => {
    const wrapper = mount(editPage, { global: { plugins: [router] } })
    await flushPromises()

    expect(wrapper.text()).toContain('Edit Kelas')
    const inputs = wrapper.findAll('input')
    const kelasInput = inputs.find((i) => i.element.value === 'Yoga')
    expect(kelasInput).toBeTruthy()
  })

  it('submits PUT with correct data', async () => {
    vi.mocked(api.put).mockResolvedValue({})
    const wrapper = mount(editPage, { global: { plugins: [router] } })
    await flushPromises()

    await wrapper.find('form').trigger('submit')
    await flushPromises()

    expect(api.put).toHaveBeenCalledWith(
      '/kelas/1',
      expect.objectContaining({
        nama_kelas: 'Yoga',
      }),
    )
  })

  it('redirects to index after successful update', async () => {
    vi.mocked(api.put).mockResolvedValue({})
    const wrapper = mount(editPage, { global: { plugins: [router] } })
    await flushPromises()

    await wrapper.find('form').trigger('submit')
    await flushPromises()
    await router.isReady()

    expect(router.currentRoute.value.name).toBe('admin.kelas.index')
  })
})
