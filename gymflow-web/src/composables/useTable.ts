import { ref, shallowRef, computed, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import api from '@/lib/axios'

export function useTable<T extends Record<string, any>>(
  endpoint: string,
  filterFn?: (item: T, search: string) => boolean,
) {
  const toast = useToast()

  const items = shallowRef<T[]>([])
  const search = ref('')
  const loading = ref(true)

  const deleteDialog = ref(false)
  const deleteLoading = ref(false)

  const toDelete = shallowRef<T | null>(null)

  const filtered = computed(() => {
    if (!filterFn || !search.value) {
      return items.value
    }

    return items.value.filter((i) => filterFn(i, search.value))
  })

  async function load() {
    loading.value = true

    try {
      const res = await api.get(endpoint)

      items.value = res.data.data
    } catch {
      toast.error('Gagal memuat data.', {
        timeout: 2000,
      })
    } finally {
      loading.value = false
    }
  }

  function confirmDelete(item: T) {
    toDelete.value = item
    deleteDialog.value = true
  }

  async function deleteItem(idKey: keyof T, deleteEndpoint?: string) {
    if (!toDelete.value) return

    deleteLoading.value = true

    try {
      const id = toDelete.value[idKey]

      await api.delete(`${deleteEndpoint ?? endpoint}/${id}`)

      items.value = items.value.filter((i) => i[idKey] !== id)

      toast.success('Berhasil Hapus Data!', {
        timeout: 2000,
      })

      deleteDialog.value = false
    } catch {
      toast.error('Gagal menghapus data.', {
        timeout: 2000,
      })
    } finally {
      deleteLoading.value = false
    }
  }

  onMounted(load)

  return {
    items,
    search,
    loading,
    filtered,

    deleteDialog,
    deleteLoading,
    toDelete,

    load,
    confirmDelete,
    deleteItem,
  }
}
