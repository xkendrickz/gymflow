import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import { createPinia, setActivePinia } from 'pinia'
import LoginPage from '@/components/LoginPage.vue'
import api from '@/lib/axios'

vi.mock('@/lib/axios')

vi.mock('vue-toastification', () => ({
  useToast: () => ({
    error: vi.fn(),
    success: vi.fn(),
  }),
}))

const router = createRouter({
  history: createMemoryHistory(),
  routes: [
    { path: '/', name: 'login', component: LoginPage },
    { path: '/admin', name: 'admin.beranda', component: { template: '<div/>' } },
    { path: '/kasir', name: 'kasir.beranda', component: { template: '<div/>' } },
  ],
})

describe('LoginPage', () => {
  beforeEach(async () => {
    vi.clearAllMocks()
    setActivePinia(createPinia())
    await router.push('/')
    await router.isReady()
  })

  it('renders login form', () => {
    const wrapper = mount(LoginPage, {
      global: { plugins: [router, createPinia()] },
    })
    expect(wrapper.find('input').exists()).toBe(true)
    expect(wrapper.find('button').exists()).toBe(true)
  })

  it('redirects admin to admin dashboard on login', async () => {
    vi.mocked(api.post).mockResolvedValue({
      data: {
        token: 'test-token',
        data: { id_role: 1, id_pegawai: 1, nama_pegawai: 'Admin Test' },
      },
    })

    const wrapper = mount(LoginPage, {
      global: { plugins: [router, createPinia()] },
    })

    const [usernameInput, passwordInput] = wrapper.findAll('input')
    await usernameInput!.setValue('admin01')
    await passwordInput!.setValue('password123')

    // trigger form submit directly - more reliable than clicking button
    await wrapper.find('form').trigger('submit')
    await flushPromises()
    await router.isReady()

    expect(router.currentRoute.value.name).toBe('admin.beranda')
  })

  it('redirects kasir to kasir dashboard on login', async () => {
    vi.mocked(api.post).mockResolvedValue({
      data: {
        token: 'test-token',
        data: { id_role: 3, id_pegawai: 2, nama_pegawai: 'Kasir Test' },
      },
    })

    const wrapper = mount(LoginPage, {
      global: { plugins: [router, createPinia()] },
    })

    const [usernameInput, passwordInput] = wrapper.findAll('input')
    await usernameInput!.setValue('kasir01')
    await passwordInput!.setValue('password123')
    await wrapper.find('form').trigger('submit')
    await flushPromises()
    await router.isReady()

    expect(router.currentRoute.value.name).toBe('kasir.beranda')
  })

  it('shows error alert on failed login', async () => {
    // component uses v-alert with loginError ref, NOT toast.error()
    vi.mocked(api.post).mockRejectedValue({
      response: { data: { message: 'Username atau password salah.' } },
    })

    const wrapper = mount(LoginPage, {
      global: { plugins: [router, createPinia()] },
    })

    await wrapper.find('form').trigger('submit')
    await flushPromises()

    expect(wrapper.text()).toContain('Username atau password salah.')
  })
})
