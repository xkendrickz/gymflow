import { ref, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import api from '@/lib/axios'

export function usePresensi(endpoint: string) {
  const toast = useToast()
  const bookings = ref<any[]>([])
  const loading = ref(true)
  const processingId = ref<number | null>(null)

  onMounted(async () => {
    try {
      const res = await api.get(`/${endpoint}`)
      bookings.value = res.data.data
    } catch {
      toast.error('Gagal memuat data.', { timeout: 2000 })
    } finally {
      loading.value = false
    }
  })

  async function markPresent(id: number, idKey: string, action: () => Promise<void>) {
    processingId.value = id
    try {
      await action()
      const item = bookings.value.find((b) => b[idKey] === id)
      if (item) item.status = 1
      toast.success('Presensi berhasil!', { timeout: 2000 })
    } catch {
      toast.error('Gagal mencatat presensi.', { timeout: 2000 })
    } finally {
      processingId.value = null
    }
  }

  return { bookings, loading, processingId, markPresent }
}
