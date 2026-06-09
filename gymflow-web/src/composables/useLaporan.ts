import { reactive, ref, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import api from '@/lib/axios'
import { useStruk } from './useStruk'

export function useLaporan(
  dropdownEndpoint: string,
  reportEndpoint: (form: any) => string,
  buildPrint: (data: any, struk: ReturnType<typeof useStruk>) => void,
  requiresBulan = true,
) {
  const toast = useToast()
  const struk = useStruk()
  const loading = ref(false)
  const dropdown = ref<any>({ months: [], years: [] })
  const form = reactive({ bulan: '', tahun: '' })

  onMounted(async () => {
    try {
      const res = await api.get(`/${dropdownEndpoint}`)
      dropdown.value = res.data.data
    } catch {
      toast.error('Gagal memuat data.', { timeout: 2000 })
    }
  })

  async function cetak() {
    if (requiresBulan && (!form.bulan || !form.tahun)) {
      toast.error('Pilih bulan dan tahun.', { timeout: 2000 })
      return
    }
    if (!requiresBulan && !form.tahun) {
      toast.error('Pilih tahun terlebih dahulu.', { timeout: 2000 })
      return
    }
    loading.value = true
    try {
      const res = await api.get(reportEndpoint(form))
      buildPrint(res.data, struk)
    } catch {
      toast.error('Gagal mengambil data laporan.', { timeout: 2000 })
    } finally {
      loading.value = false
    }
  }

  return { form, dropdown, loading, cetak }
}
