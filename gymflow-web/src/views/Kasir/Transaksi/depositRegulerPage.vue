<template>
	<div class="page-header">
		<h2 class="page-title">Deposit Kelas</h2>
		<p class="page-subtitle">Proses deposit paket kelas member</p>
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
					<div>
						<p class="field-label">Kelas</p>
						<v-select v-model="form.id_kelas" :items="kelass" item-title="nama_kelas" item-value="id_kelas"
							placeholder="Pilih kelas" variant="outlined" density="comfortable" color="orange"
							bg-color="#1a1a1a" :error-messages="errors.id_kelas" />
					</div>
					<div>
						<p class="field-label">Jumlah Deposit</p>
						<v-select v-model="form.deposit"
							:items="[{ title: '5 sesi', value: '5' }, { title: '10 sesi', value: '10' }]"
							item-title="title" item-value="value" placeholder="Pilih jumlah deposit" variant="outlined"
							density="comfortable" color="orange" bg-color="#1a1a1a" :error-messages="errors.deposit" />
					</div>
				</div>
				<v-divider class="my-6" style="border-color: rgba(255,255,255,0.06);" />
				<div class="form-actions">
					<v-btn type="submit" :loading="loading" rounded="lg" class="save-btn"
						prepend-icon="mdi-cash-multiple">
						Proses Deposit
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
const kelass = ref<any[]>([])

const form = ref({ id_member: '', id_pegawai: '', id_kelas: '', deposit: '' })
const errors = ref<Record<string, string[]>>({})

onMounted(async () => {
	try {
		const [r1, r2, r3] = await Promise.all([
			api.get('/member'), api.get('/pegawai'), api.get('/kelas')
		])
		members.value = r1.data.data
		pegawais.value = r2.data.data
		kelass.value = r3.data.data
	} catch { toast.error('Gagal memuat data.', { timeout: 2000 }) }
})

async function store() {
	loading.value = true
	errors.value = {}
	try {
		const res = await api.post('/depositKelas', form.value)
		const detail = await api.get(`/depositKelas/${res.data.data.id_deposit_paket}`)
		printStruk('depositKelas', detail.data.data)
		toast.success('Berhasil Deposit Kelas!', { timeout: 2000 })
		form.value = { id_member: '', id_pegawai: '', id_kelas: '', deposit: '' }
	} catch (e: any) {
		if (e.response?.data?.errors) errors.value = e.response.data.errors
		else toast.error('Gagal memproses deposit.', { timeout: 2000 })
	} finally { loading.value = false }
}
</script>