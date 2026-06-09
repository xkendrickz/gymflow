<template>
	<div class="page-header">
		<h2 class="page-title">Presensi Gym</h2>
		<p class="page-subtitle">Daftar booking gym hari ini</p>
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
							<th>Nama Member</th>
							<th>Tanggal</th>
							<th>Slot Waktu</th>
							<th>Status</th>
							<th>Aksi</th>
						</tr>
					</thead>
					<tbody>
						<tr v-if="bookings.length === 0">
							<td colspan="5" class="empty-row">Tidak ada data booking.</td>
						</tr>
						<tr v-for="item in bookings" :key="item.id_booking_gym">
							<td>{{ item.nama_member }}</td>
							<td>{{ item.tanggal }}</td>
							<td>{{ item.slot_waktu }}</td>
							<td>
								<span :class="['status-badge', item.status == 1 ? 'status-active' : 'status-inactive']">
									{{ item.status == 1 ? 'Hadir' : 'Belum' }}
								</span>
							</td>
							<td>
								<v-btn v-if="item.status != 1" size="small" rounded="lg" class="presensi-btn"
									:loading="processingId === item.id_booking_gym"
									@click="presensi(item.id_booking_gym)">
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

const { bookings, loading, processingId, markPresent } = usePresensi('presensiGym')
const { printStruk } = useStruk()

async function presensi(id: number) {
	await markPresent(id, 'id_booking_gym', async () => {
		const res = await api.put(`/presensiGym/${id}`)
		const idPresensi = res.data.data.id_presensi_gym
		const detail = await api.get(`/presensiGym/${idPresensi}`)
		printStruk('gym', detail.data.data)
	})
}
</script>