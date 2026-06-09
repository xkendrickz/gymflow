import { describe, it, expect, vi } from 'vitest'
import { useForm } from '@/composables/useForm'
import api from '@/lib/axios'

vi.mock('@/lib/axios')
vi.mock('vue-router', () => ({
  useRouter: () => ({ push: vi.fn() }),
}))
vi.mock('vue-toastification', () => ({
  useToast: () => ({ error: vi.fn(), success: vi.fn() }),
}))

describe('useForm', () => {
  it('initializes form with given values', () => {
    const { form } = useForm({ nama_kelas: '', tarif: '' })
    expect(form.nama_kelas).toBe('')
    expect(form.tarif).toBe('')
  })

  it('submits form and calls api with correct method', async () => {
    vi.mocked(api.post).mockResolvedValue({})
    const { form, submit } = useForm({ nama_kelas: '', tarif: '' })

    form.nama_kelas = 'Yoga'
    form.tarif = '50000'

    await submit('post', '/kelas', 'Berhasil!', 'admin.kelas.index')

    expect(api.post).toHaveBeenCalledWith('/kelas', {
      nama_kelas: 'Yoga',
      tarif: '50000',
    })
  })

  it('stores validation errors on 422 response', async () => {
    vi.mocked(api.post).mockRejectedValue({
      response: {
        data: {
          errors: { nama_kelas: ['Nama kelas wajib diisi.'] },
        },
      },
    })

    const { errors, submit } = useForm({ nama_kelas: '', tarif: '' })
    await submit('post', '/kelas', 'Berhasil!')

    expect(errors.nama_kelas).toEqual(['Nama kelas wajib diisi.'])
  })

  it('resets form after successful submit without redirect', async () => {
    vi.mocked(api.post).mockResolvedValue({})
    const { form, submit } = useForm({ nama_kelas: '', tarif: '' })

    form.nama_kelas = 'Yoga'
    await submit('post', '/kelas', 'Berhasil!')

    expect(form.nama_kelas).toBe('')
  })
})
