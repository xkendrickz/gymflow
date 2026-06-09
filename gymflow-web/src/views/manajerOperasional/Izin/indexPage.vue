<template>
	<div class="page-header">
		<h2 class="page-title">Izin Instruktur</h2>
		<p class="page-subtitle">Kelola pengajuan izin instruktur</p>
	</div>

	<div v-if="loading" class="d-flex justify-center pa-8">
		<v-progress-circular indeterminate color="orange" />
	</div>

	<template v-else>
		<!-- Confirmed -->
		<v-card class="table-card mb-4" rounded="xl">
			<v-card-text class="pa-6">
				<p class="section-label">✓ SUDAH DIKONFIRMASI</p>
				<div class="table-wrapper">
					<table class="data-table">
						<thead>
							<tr>
								<th>Instruktur</th>
								<th>Detail Izin</th>
								<th>Tanggal</th>
								<th>Status</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="confirmed.length === 0">
								<td colspan="4" class="empty-row">Tidak ada izin terkonfirmasi.</td>
							</tr>
							<tr v-for="item in confirmed" :key="item.id_izin">
								<td>{{ item.nama_instruktur }}</td>
								<td>{{ item.detail_izin }}</td>
								<td>{{ item.tanggal_izin }}</td>
								<td><span class="status-badge status-active">Dikonfirmasi</span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</v-card-text>
		</v-card>

		<!-- Unconfirmed -->
		<v-card class="table-card" rounded="xl">
			<v-card-text class="pa-6">
				<p class="section-label">⏳ MENUNGGU KONFIRMASI</p>
				<div class="table-wrapper">
					<table class="data-table">
						<thead>
							<tr>
								<th>Instruktur</th>
								<th>Detail Izin</th>
								<th>Tanggal</th>
								<th>Status</th>
								<th>Aksi</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="unconfirmed.length === 0">
								<td colspan="5" class="empty-row">Tidak ada pengajuan baru.</td>
							</tr>
							<tr v-for="item in unconfirmed" :key="item.id_izin">
								<td>{{ item.nama_instruktur }}</td>
								<td>{{ item.detail_izin }}</td>
								<td>{{ item.tanggal_izin }}</td>
								<td><span class="status-badge status-pending">Menunggu</span></td>
								<td>
									<v-btn size="small" rounded="lg" class="konfirmasi-btn"
										:loading="processingId === item.id_izin" @click="konfirmasi(item.id_izin)">
										Konfirmasi
									</v-btn>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</v-card-text>
		</v-card>
	</template>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import api from '@/lib/axios'

const toast = useToast()
const izin = ref<any[]>([])
const loading = ref(true)
const processingId = ref<number | null>(null)

const confirmed = computed(() => izin.value.filter(i => i.konfirmasi === 1))
const unconfirmed = computed(() => izin.value.filter(i => i.konfirmasi === 0))

onMounted(async () => {
	try {
		const res = await api.get('/izin')
		izin.value = res.data.data
	} catch { toast.error('Gagal memuat data.', { timeout: 2000 }) }
	finally { loading.value = false }
})

async function konfirmasi(id: number) {
	processingId.value = id
	try {
		await api.put(`/izin/${id}`)
		const item = izin.value.find(i => i.id_izin === id)
		if (item) item.konfirmasi = 1
		toast.success('Izin berhasil dikonfirmasi!', { timeout: 2000 })
	} catch { toast.error('Gagal konfirmasi izin.', { timeout: 2000 }) }
	finally { processingId.value = null }
}
</script>