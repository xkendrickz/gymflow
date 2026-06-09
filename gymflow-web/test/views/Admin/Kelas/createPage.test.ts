import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import createPage from '@/views/Admin/Kelas/createPage.vue'
import api from '@/lib/axios'

vi.mock('@/lib/axios')
vi.mock('vue-toastification', () => ({
  useToast: () => ({ error: vi.fn(), success: vi.fn() }),
}))

const router = createRouter({
  history: createMemoryHistory(),
  routes: [
    { path: '/admin/kelas/create', name: 'admin.kelas.create', component: createPage },
    { path: '/admin/kelas', name: 'admin.kelas.index', component: { template: '<div/>' } },
  ],
})

describe('Kelas createPage', () => {
  beforeEach(async () => {
    vi.clearAllMocks()
    await router.push('/admin/kelas/create')
    await router.isReady()
  })

  it('renders form fields', () => {
    const wrapper = mount(createPage, {
      global: { plugins: [router] },
    })
    expect(wrapper.text()).toContain('Nama Kelas')
    expect(wrapper.text()).toContain('Tarif')
    expect(wrapper.text()).toContain('Simpan')
  })

  it('submits form with correct data', async () => {
    vi.mocked(api.post).mockResolvedValue({})

    const wrapper = mount(createPage, {
      global: { plugins: [router] },
    })

    const [kelasInput, priceInput] = wrapper.findAll('input')
    await kelasInput!.setValue('Pilates')
    await priceInput!.setValue('60000')

    // trigger form submit directly instead of clicking button
    await wrapper.find('form').trigger('submit')
    await flushPromises()

    expect(api.post).toHaveBeenCalledWith('/kelas', {
      nama_kelas: 'Pilates',
      tarif: '60000',
    })
  })

  it('shows validation errors from API', async () => {
    vi.mocked(api.post).mockRejectedValue({
      response: {
        data: {
          errors: { nama_kelas: ['Nama kelas wajib diisi.'] },
        },
      },
    })

    const wrapper = mount(createPage, {
      global: { plugins: [router] },
    })

    await wrapper.find('form').trigger('submit')
    await flushPromises()

    // check the error is in the form's reactive state
    // Vuetify renders errors inside .v-messages
    expect(wrapper.html()).toContain('Nama kelas wajib diisi.')
  })

  it('redirects to index after successful submit', async () => {
    vi.mocked(api.post).mockResolvedValue({})

    const wrapper = mount(createPage, {
      global: { plugins: [router] },
    })

    const [kelasInput, priceInput] = wrapper.findAll('input')
    await kelasInput!.setValue('Pilates')
    await priceInput!.setValue('60000')
    await wrapper.find('form').trigger('submit')
    await flushPromises()

    expect(router.currentRoute.value.name).toBe('admin.kelas.index')
  })
})
