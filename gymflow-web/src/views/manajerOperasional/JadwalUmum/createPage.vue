<template>
	<div class="page-header">
		<h2 class="page-title">Tambah Jadwal Umum</h2>
		<p class="page-subtitle">Tambah jadwal kelas mingguan</p>
	</div>

	<v-card class="form-card" rounded="xl">
		<v-card-text class="pa-6">
			<form @submit.prevent="store">
				<div class="form-grid">
					<div>
						<p class="field-label">Hari</p>
						<v-select v-model="form.hari"
							:items="['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday']"
							placeholder="Pilih hari" variant="outlined" density="comfortable" color="orange"
							bg-color="#1a1a1a" :error-messages="errors.hari" />
					</div>
					<div>
						<p class="field-label">Kelas</p>
						<v-select v-model="form.id_kelas" :items="kelass" item-title="nama_kelas" item-value="id_kelas"
							placeholder="Pilih kelas" variant="outlined" density="comfortable" color="orange"
							bg-color="#1a1a1a" :error-messages="errors.id_kelas" />
					</div>
					<div>
						<p class="field-label">Instruktur</p>
						<v-select v-model="form.id_instruktur" :items="instrukturs" item-title="nama_instruktur"
							item-value="id_instruktur" placeholder="Pilih instruktur" variant="outlined"
							density="comfortable" color="orange" bg-color="#1a1a1a"
							:error-messages="errors.id_instruktur" />
					</div>
					<div>
						<p class="field-label">Jam</p>
						<v-text-field v-model="form.jam" type="time" variant="outlined" density="comfortable"
							color="orange" bg-color="#1a1a1a" :error-messages="errors.jam" />
					</div>
				</div>
				<v-divider class="my-6" style="border-color: rgba(255,255,255,0.06);" />
				<div class="form-actions">
					<v-btn variant="outlined" color="grey" rounded="lg"
						@click="router.push({ name: 'admin.jadwalUmum.index' })">Batal</v-btn>
					<v-btn type="submit" :loading="loading" rounded="lg" class="save-btn">
						<v-icon start>mdi-content-save</v-icon>Simpan
					</v-btn>
				</div>
			</form>
		</v-card-text>
	</v-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useForm } from '@/composables/useForm'
import api from '@/lib/axios'

const router = useRouter()
const toast = useToast()
const kelass = ref<any[]>([])
const instrukturs = ref<any[]>([])

const { form, errors, loading, submit } = useForm({
	hari: '', id_kelas: '', id_instruktur: '', jam: ''
})

onMounted(async () => {
	try {
		const [r1, r2] = await Promise.all([api.get('/kelas'), api.get('/instruktur')])
		kelass.value = r1.data.data
		instrukturs.value = r2.data.data
	} catch { toast.error('Gagal memuat data.', { timeout: 2000 }) }
})

const store = () => submit('post', '/jadwalUmum', 'Berhasil Tambah Jadwal!', 'mo.jadwalUmum.index')
</script>