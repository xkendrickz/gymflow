import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import editPage from '@/views/Admin/Instruktur/editPage.vue'
import api from '@/lib/axios'

vi.mock('@/lib/axios')
vi.mock('vue-toastification', () => ({
  useToast: () => ({ error: vi.fn(), success: vi.fn() }),
}))

const router = createRouter({
  history: createMemoryHistory(),
  routes: [
    { path: '/admin/instruktur/edit/:id', name: 'admin.instruktur.edit', component: editPage },
    {
      path: '/admin/instruktur',
      name: 'admin.instruktur.index',
      component: { template: '<div/>' },
    },
  ],
})

describe('Instruktur editPage', () => {
  beforeEach(async () => {
    vi.clearAllMocks()
    vi.mocked(api.get).mockResolvedValue({
      data: {
        data: {
          nama_instruktur: 'Andi Wijaya',
          tanggal_lahir: '1990-03-15',
          username: 'andi01',
        },
      },
    })
    await router.push('/admin/instruktur/edit/1')
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

    const inputs = wrapper.findAll('input')
    const namaInput = inputs.find((i) => i.element.value === 'Andi Wijaya')
    expect(namaInput).toBeTruthy()
  })

  it('submits PUT with correct data', async () => {
    vi.mocked(api.put).mockResolvedValue({})
    const wrapper = mount(editPage, { global: { plugins: [router] } })
    await flushPromises()

    await wrapper.find('form').trigger('submit')
    await flushPromises()

    expect(api.put).toHaveBeenCalledWith(
      '/instruktur/1',
      expect.objectContaining({
        nama_instruktur: 'Andi Wijaya',
        username: 'andi01',
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

    expect(router.currentRoute.value.name).toBe('admin.instruktur.index')
  })
})
