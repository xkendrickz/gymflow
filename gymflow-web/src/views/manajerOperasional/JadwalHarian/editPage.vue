<template>
	<div class="page-header">
		<h2 class="page-title">Edit Jadwal Harian</h2>
		<p class="page-subtitle">Ubah status kelas hari ini</p>
	</div>

	<v-card class="form-card" rounded="xl">
		<v-card-text class="pa-6">
			<div v-if="fetchLoading" class="d-flex justify-center pa-8">
				<v-progress-circular indeterminate color="orange" />
			</div>
			<form v-else @submit.prevent="update">
				<div>
					<p class="field-label">Status Kelas</p>
					<v-select v-model="form.status" :items="['Libur', 'Digantikan']" placeholder="Pilih status"
						variant="outlined" density="comfortable" color="orange" bg-color="#1a1a1a"
						:error-messages="errors.status" />
				</div>
				<v-divider class="my-6" style="border-color: rgba(255,255,255,0.06);" />
				<div class="form-actions">
					<v-btn variant="outlined" color="grey" rounded="lg"
						@click="router.push({ name: 'admin.jadwalHarian.index' })">Batal</v-btn>
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

const { form, errors, loading, fetchLoading, fetchData, submit } = useForm({ status: '' })

fetchData(`/jadwalHarian/${id}`, data => ({
	status: data[0]?.status ?? ''
}))

const update = () => submit('put', `/jadwalHarian/${id}`, 'Berhasil Edit Jadwal!', 'mo.jadwalHarian.index')
</script>