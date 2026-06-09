import { ref } from 'vue'

export function useDeleteDialog<T>() {
  const deleteDialog = ref(false)
  const deleteLoading = ref(false)
  const toDelete = ref<T | null>(null)

  function open(item: T) {
    toDelete.value = item
    deleteDialog.value = true
  }

  function close() {
    deleteDialog.value = false
    toDelete.value = null
  }

  return { deleteDialog, deleteLoading, toDelete, open, close }
}
