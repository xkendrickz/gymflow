import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import indexPage from '@/views/Admin/Kelas/indexPage.vue'
import api from '@/lib/axios'

vi.mock('@/lib/axios')
vi.mock('vue-toastification', () => ({
  useToast: () => ({ error: vi.fn(), success: vi.fn() }),
}))

const router = createRouter({
  history: createMemoryHistory(),
  routes: [
    { path: '/', component: indexPage },
    { path: '/admin/kelas/create', name: 'admin.kelas.create', component: { template: '<div/>' } },
    { path: '/admin/kelas/edit/:id', name: 'admin.kelas.edit', component: { template: '<div/>' } },
  ],
})

describe('Kelas indexPage', () => {
  beforeEach(() => {
    vi.mocked(api.get).mockResolvedValue({
      data: {
        data: [
          { id_kelas: 1, nama_kelas: 'Yoga', tarif: 50000 },
          { id_kelas: 2, nama_kelas: 'Zumba', tarif: 75000 },
        ],
      },
    })
  })

  it('shows loading spinner initially', () => {
    const wrapper = mount(indexPage, {
      global: { plugins: [router] },
    })
    expect(wrapper.find('.v-progress-circular').exists()).toBe(true)
  })

  it('renders kelas list after loading', async () => {
    const wrapper = mount(indexPage, {
      global: { plugins: [router] },
    })
    await new Promise((r) => setTimeout(r, 0))
    await wrapper.vm.$nextTick()

    expect(wrapper.text()).toContain('Yoga')
    expect(wrapper.text()).toContain('Zumba')
  })

  it('formats tarif as IDR currency', async () => {
    const wrapper = mount(indexPage, {
      global: { plugins: [router] },
    })
    await new Promise((r) => setTimeout(r, 0))
    await wrapper.vm.$nextTick()

    expect(wrapper.text()).toContain('Rp')
  })

  it('shows empty message when no kelas', async () => {
    vi.mocked(api.get).mockResolvedValue({ data: { data: [] } })

    const wrapper = mount(indexPage, {
      global: { plugins: [router] },
    })
    await new Promise((r) => setTimeout(r, 0))
    await wrapper.vm.$nextTick()

    expect(wrapper.text()).toContain('Tidak ada data kelas.')
  })
})
