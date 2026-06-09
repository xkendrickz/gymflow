<template>
	<div class="page-header">
		<h2 class="page-title">Edit Jadwal Umum</h2>
		<p class="page-subtitle">Ubah hari dan jam kelas</p>
	</div>

	<v-card class="form-card" rounded="xl">
		<v-card-text class="pa-6">
			<div v-if="fetchLoading" class="d-flex justify-center pa-8">
				<v-progress-circular indeterminate color="orange" />
			</div>
			<form v-else @submit.prevent="update">
				<div class="form-grid">
					<div>
						<p class="field-label">Hari</p>
						<v-select v-model="form.hari"
							:items="['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday']"
							variant="outlined" density="comfortable" color="orange" bg-color="#1a1a1a"
							:error-messages="errors.hari" />
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
import { useRouter, useRoute } from 'vue-router'
import { useForm } from '@/composables/useForm'

const router = useRouter()
const route = useRoute()
const id = route.params.id

const { form, errors, loading, fetchLoading, fetchData, submit } = useForm({ hari: '', jam: '' })

fetchData(`/jadwalUmum/${id}`, data => ({
	hari: data.hari,
	jam: data.jam,
}))

const update = () => submit('put', `/jadwalUmum/${id}`, 'Berhasil Edit Jadwal!', 'mo.jadwalUmum.index')
</script>