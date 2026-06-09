import { describe, it, expect, vi, beforeEach } from 'vitest'
import { defineComponent, h } from 'vue'
import { mount } from '@vue/test-utils'
import { useTable } from '@/composables/useTable'
import api from '@/lib/axios'

vi.mock('@/lib/axios')
vi.mock('vue-toastification', () => ({
  useToast: () => ({ error: vi.fn(), success: vi.fn() }),
}))

// helper: mounts a real component so onMounted fires
function withSetup<T>(composable: () => T): T {
  let result: T
  const TestComponent = defineComponent({
    setup() {
      result = composable()
      return () => h('div')
    },
  })
  mount(TestComponent)
  return result!
}

describe('useTable', () => {
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

  it('loads data on mount', async () => {
    const { items, loading } = withSetup(() => useTable('/kelas'))

    await new Promise((r) => setTimeout(r, 0))

    expect(items.value).toHaveLength(2)
    expect(loading.value).toBe(false)
  })

  it('filters items by search', async () => {
    const { filtered, search } = withSetup(() =>
      useTable<any>('/kelas', (item, s) => item.nama_kelas.toLowerCase().includes(s.toLowerCase())),
    )

    await new Promise((r) => setTimeout(r, 0))

    search.value = 'yoga'
    expect(filtered.value).toHaveLength(1)
    expect(filtered.value[0].nama_kelas).toBe('Yoga')
  })

  it('sets toDelete and opens dialog on confirmDelete', async () => {
    const { deleteDialog, toDelete, confirmDelete } = withSetup(() => useTable<any>('/kelas'))

    await new Promise((r) => setTimeout(r, 0))

    const item = { id_kelas: 1, nama_kelas: 'Yoga' }
    confirmDelete(item)

    expect(toDelete.value).toEqual(item)
    expect(deleteDialog.value).toBe(true)
  })

  it('removes item from list after delete', async () => {
    vi.mocked(api.delete).mockResolvedValue({})

    const { items, deleteDialog, toDelete, confirmDelete, deleteItem } = withSetup(() =>
      useTable<any>('/kelas'),
    )

    await new Promise((r) => setTimeout(r, 0))

    confirmDelete({ id_kelas: 1, nama_kelas: 'Yoga' })
    await deleteItem('id_kelas')

    expect(items.value).toHaveLength(1)
    expect(items.value[0].nama_kelas).toBe('Zumba')
    expect(deleteDialog.value).toBe(false)
  })
})
