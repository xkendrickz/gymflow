<template>
	<div class="page-header">
		<h2 class="page-title">Aktivasi Member</h2>
		<p class="page-subtitle">Proses aktivasi tahunan member</p>
	</div>

	<v-card class="form-card" rounded="xl">
		<v-card-text class="pa-6">
			<form @submit.prevent="store">
				<div class="form-grid">
					<div>
						<p class="field-label">Member</p>
						<v-select v-model="form.id_member" :items="members" item-title="nama_member"
							item-value="id_member" placeholder="Pilih member" variant="outlined" density="comfortable"
							color="orange" bg-color="#1a1a1a" :error-messages="errors.id_member" />
					</div>
					<div>
						<p class="field-label">Kasir</p>
						<v-select v-model="form.id_pegawai" :items="pegawais" item-title="nama_pegawai"
							item-value="id_pegawai" placeholder="Pilih kasir" variant="outlined" density="comfortable"
							color="orange" bg-color="#1a1a1a" :error-messages="errors.id_pegawai" />
					</div>
				</div>
				<v-divider class="my-6" style="border-color: rgba(255,255,255,0.06);" />
				<div class="form-actions">
					<v-btn type="submit" :loading="loading" rounded="lg" class="save-btn" prepend-icon="mdi-check">
						Proses Aktivasi
					</v-btn>
				</div>
			</form>
		</v-card-text>
	</v-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import { useStruk } from '@/composables/useStruk'
import api from '@/lib/axios'

const toast = useToast()
const { printStruk } = useStruk()
const loading = ref(false)
const members = ref<any[]>([])
const pegawais = ref<any[]>([])
const form = { id_member: '', id_pegawai: '' }
const errors = {} as Record<string, string[]>

onMounted(async () => {
	try {
		const [r1, r2] = await Promise.all([api.get('/member'), api.get('/pegawai')])
		members.value = r1.data.data
		pegawais.value = r2.data.data
	} catch { toast.error('Gagal memuat data.', { timeout: 2000 }) }
})

async function store() {
	loading.value = true
	try {
		const res = await api.post('/aktivasi', form)
		const detail = await api.get(`/aktivasi/${res.data.data.id_aktivasi}`)
		printStruk('aktivasi', detail.data.data)
		toast.success('Berhasil Aktivasi Member!', { timeout: 2000 })
		form.id_member = ''; form.id_pegawai = ''
	} catch (e: any) {
		if (e.response?.data?.errors) Object.assign(errors, e.response.data.errors)
		else toast.error('Gagal memproses aktivasi.', { timeout: 2000 })
	} finally { loading.value = false }
}
</script>