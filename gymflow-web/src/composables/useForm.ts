import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import api from '@/lib/axios'

export function useForm<T extends Record<string, any>>(initialForm: T) {
  const toast = useToast()
  const router = useRouter()
  const loading = ref(false)
  const fetchLoading = ref(false)
  const form = reactive<T>({ ...initialForm })
  const errors = reactive<Record<string, string[]>>({})

  function clearErrors() {
    Object.keys(errors).forEach((k) => delete errors[k])
  }

  function resetForm() {
    Object.assign(form, initialForm)
    clearErrors()
  }

  async function fetchData(endpoint: string, mapFn: (data: any) => Partial<T>) {
    fetchLoading.value = true
    try {
      const res = await api.get(endpoint)
      Object.assign(form, mapFn(res.data.data))
    } catch {
      toast.error('Gagal memuat data.', { timeout: 2000 })
    } finally {
      fetchLoading.value = false
    }
  }

  async function submit(
    method: 'post' | 'put',
    endpoint: string,
    successMessage: string,
    redirectName?: string,
  ) {
    loading.value = true
    clearErrors()
    try {
      await api[method](endpoint, form)
      toast.success(successMessage, { timeout: 2000 })
      if (redirectName) router.push({ name: redirectName })
      else resetForm()
    } catch (e: any) {
      if (e.response?.data?.errors) {
        Object.assign(errors, e.response.data.errors)
      } else {
        toast.error('Gagal menyimpan data.', { timeout: 2000 })
      }
    } finally {
      loading.value = false
    }
  }

  return { form, errors, loading, fetchLoading, submit, fetchData, resetForm }
}
