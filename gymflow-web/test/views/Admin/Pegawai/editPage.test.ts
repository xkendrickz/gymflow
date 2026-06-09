import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import editPage from '@/views/Admin/Pegawai/editPage.vue'
import api from '@/lib/axios'

vi.mock('@/lib/axios')
vi.mock('vue-toastification', () => ({
  useToast: () => ({ error: vi.fn(), success: vi.fn() }),
}))

const router = createRouter({
  history: createMemoryHistory(),
  routes: [
    { path: '/admin/pegawai/edit/:id', name: 'admin.pegawai.edit', component: editPage },
    { path: '/admin/pegawai', name: 'admin.pegawai.index', component: { template: '<div/>' } },
  ],
})

describe('Pegawai editPage', () => {
  beforeEach(async () => {
    vi.clearAllMocks()
    vi.mocked(api.get).mockResolvedValue({
      data: {
        data: {
          nama_pegawai: 'Kasir Satu',
          tanggal_lahir: '1995-05-10',
          id_role: 3,
          username: 'kasir01',
        },
      },
    })
    await router.push('/admin/pegawai/edit/1')
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
    const namaInput = inputs.find((i) => i.element.value === 'Kasir Satu')
    expect(namaInput).toBeTruthy()
  })

  it('submits PUT with correct data', async () => {
    vi.mocked(api.put).mockResolvedValue({})
    const wrapper = mount(editPage, { global: { plugins: [router] } })
    await flushPromises()

    await wrapper.find('form').trigger('submit')
    await flushPromises()

    expect(api.put).toHaveBeenCalledWith(
      '/pegawai/1',
      expect.objectContaining({
        nama_pegawai: 'Kasir Satu',
        username: 'kasir01',
        id_role: 3,
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

    expect(router.currentRoute.value.name).toBe('admin.pegawai.index')
  })
})
