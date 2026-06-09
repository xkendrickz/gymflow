<template>
	<div class="page-header">
		<h2 class="page-title">Presensi Kelas</h2>
		<p class="page-subtitle">Daftar booking kelas hari ini</p>
	</div>

	<v-card class="table-card" rounded="xl">
		<v-card-text class="pa-6">
			<div v-if="loading" class="d-flex justify-center pa-8">
				<v-progress-circular indeterminate color="orange" />
			</div>
			<div v-else class="table-wrapper">
				<table class="data-table">
					<thead>
						<tr>
							<th>Kelas</th>
							<th>Instruktur</th>
							<th>Member</th>
							<th>Hari</th>
							<th>Jenis</th>
							<th>Status</th>
							<th>Aksi</th>
						</tr>
					</thead>
					<tbody>
						<tr v-if="bookings.length === 0">
							<td colspan="7" class="empty-row">Tidak ada data booking.</td>
						</tr>
						<tr v-for="item in bookings" :key="item.id_presensi_kelas">
							<td>{{ item.nama_kelas }}</td>
							<td>{{ item.nama_instruktur }}</td>
							<td>{{ item.nama_member }}</td>
							<td>{{ item.hari }}</td>
							<td>
								<span
									:class="['jenis-badge', item.jenis === 'Paket' ? 'jenis-paket' : 'jenis-reguler']">
									{{ item.jenis }}
								</span>
							</td>
							<td>
								<span :class="['status-badge', item.status == 1 ? 'status-active' : 'status-inactive']">
									{{ item.status == 1 ? 'Hadir' : 'Belum' }}
								</span>
							</td>
							<td>
								<v-btn v-if="item.status != 1" size="small" rounded="lg" class="presensi-btn"
									:loading="processingId === item.id_presensi_kelas"
									@click="presensi(item.id_presensi_kelas)">
									Presensi
								</v-btn>
								<span v-else style="color: #22c55e; font-size: 0.8rem;">✓ Tercatat</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</v-card-text>
	</v-card>
</template>

<script setup lang="ts">
import { usePresensi } from '@/composables/usePresensi'
import { useStruk } from '@/composables/useStruk'
import api from '@/lib/axios'

const { bookings, loading, processingId, markPresent } = usePresensi('presensiKelas')
const { printStruk } = useStruk()

async function presensi(id: number) {
	await markPresent(id, 'id_presensi_kelas', async () => {
		const res = await api.get(`/cetakStruk/${id}`)
		printStruk('kelas', res.data.data)
	})
}
</script>