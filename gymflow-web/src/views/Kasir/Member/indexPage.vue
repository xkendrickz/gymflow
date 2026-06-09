<template>
	<div class="page-header">
		<div>
			<h2 class="page-title">Member</h2>
			<p class="page-subtitle">Manage all registered gym members</p>
		</div>
		<v-btn :to="{ name: 'kasir.member.create' }" rounded="lg" class="new-btn" prepend-icon="mdi-plus">
			New Member
		</v-btn>
	</div>

	<v-card class="table-card" rounded="xl">
		<v-card-text class="pa-6">

			<!-- Search -->
			<v-text-field v-model="search" prepend-inner-icon="mdi-magnify" placeholder="Cari nama member..."
				variant="outlined" density="comfortable" color="orange" bg-color="#1a1a1a" class="mb-4"
				style="max-width: 400px;" hide-details />

			<!-- Loading -->
			<div v-if="loading" class="d-flex justify-center pa-8">
				<v-progress-circular indeterminate color="orange" />
			</div>

			<!-- Table -->
			<div v-else class="table-wrapper">
				<table class="data-table">
					<thead>
						<tr>
							<th>Member ID</th>
							<th>Nama</th>
							<th>Status</th>
							<th>Telepon</th>
							<th>Email</th>
							<th>Tanggal Lahir</th>
							<th>Tanggal Daftar</th>
							<th>Aksi</th>
						</tr>
					</thead>
					<tbody>
						<tr v-if="filtered.length === 0">
							<td colspan="8" class="empty-row">Tidak ada data member.</td>
						</tr>
						<tr v-for="item in filtered" :key="item.id_member">
							<td><span class="member-id">{{ item.member_id }}</span></td>
							<td class="font-weight-medium">{{ item.nama_member }}</td>
							<td>
								<span
									:class="['status-badge', item.status === 'aktif' ? 'status-active' : 'status-inactive']">
									{{ item.status }}
								</span>
							</td>
							<td>{{ item.telepon ?? '—' }}</td>
							<td>{{ item.email ?? '—' }}</td>
							<td>{{ item.tanggal_lahir ?? '—' }}</td>
							<td>{{ item.tanggal_daftar ?? '—' }}</td>
							<td>
								<div class="action-btns">
									<v-btn :to="{ name: 'kasir.member.edit', params: { id: item.id_member } }" icon
										variant="text" color="blue" size="small">
										<v-icon size="18">mdi-pencil</v-icon>
									</v-btn>
									<v-btn icon variant="text" color="red" size="small" @click="confirmDelete(item)">
										<v-icon size="18">mdi-trash-can-outline</v-icon>
									</v-btn>
									<v-btn icon variant="text" color="orange" size="small"
										@click="printCard(item.id_member)">
										<v-icon size="18">mdi-card-account-details</v-icon>
									</v-btn>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>

		</v-card-text>
	</v-card>

	<!-- Delete confirm dialog -->
	<v-dialog v-model="deleteDialog" max-width="400">
		<v-card rounded="xl" style="background: #111111; border: 1px solid rgba(255,255,255,0.06);">
			<v-card-text class="pa-6">
				<div class="d-flex align-center gap-3 mb-4">
					<div class="delete-icon-wrap">
						<v-icon color="red" size="24">mdi-alert-circle</v-icon>
					</div>
					<div>
						<p class="dialog-title">Hapus Member</p>
						<p class="dialog-subtitle">Tindakan ini tidak dapat dibatalkan.</p>
					</div>
				</div>
				<p class="text-sm" style="color: #9ca3af;">
					Yakin ingin menghapus member <strong style="color: #f1f5f9;">{{ toDelete?.nama_member }}</strong>?
				</p>
			</v-card-text>
			<v-card-actions class="px-6 pb-6 pt-0 gap-3">
				<v-spacer />
				<v-btn variant="outlined" color="grey" rounded="lg" @click="deleteDialog = false">Batal</v-btn>
				<v-btn color="red" variant="flat" rounded="lg" :loading="deleteLoading"
					@click="deleteItem('id_member')">Hapus</v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script setup lang="ts">
import { useToast } from 'vue-toastification'
import { useTable } from '@/composables/useTable'
import api from '@/lib/axios'

const toast = useToast()

const { search, loading, filtered, deleteDialog, deleteLoading, toDelete, confirmDelete, deleteItem }
	= useTable<any>('/member', (m, s) => m.nama_member?.toLowerCase().includes(s.toLowerCase()),)

async function printCard(id: number) {
	try {
		const res = await api.get(`/member/${id}`)
		const item = res.data.data
		const popup = window.open('', '_blank')
		if (!popup) return
		popup.document.write(`
      <html><head><title>Member Card</title>
      <style>
        body { font-family: 'Segoe UI', sans-serif; background: #f5f5f5; display: flex; justify-content: center; padding: 40px; }
        .card { width: 320px; background: white; border-radius: 12px; padding: 24px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .gym-name { font-size: 20px; font-weight: 800; color: #f97316; margin: 0 0 2px; }
        .address { font-size: 11px; color: #9ca3af; margin: 0 0 16px; }
        .divider { border: none; border-top: 1px solid #e5e7eb; margin: 12px 0; }
        .label { font-size: 10px; color: #9ca3af; text-transform: uppercase; letter-spacing: 0.05em; }
        .value { font-size: 13px; color: #111; font-weight: 500; margin-bottom: 8px; }
        .badge { display: inline-block; background: #f97316; color: white; font-size: 10px; padding: 2px 10px; border-radius: 99px; font-weight: 600; }
      </style></head><body>
      <div class="card">
        <p class="gym-name">GymFlow</p>
        <p class="address">Jl. Qlipoth Tree No. 5, Yogyakarta</p>
        <span class="badge">MEMBER CARD</span>
        <hr class="divider">
        <p class="label">Member ID</p><p class="value">${item.member_id}</p>
        <p class="label">Nama</p><p class="value">${item.nama_member}</p>
        <p class="label">Alamat</p><p class="value">${item.alamat ?? '—'}</p>
        <p class="label">Telepon</p><p class="value">${item.telepon ?? '—'}</p>
      </div>
      </body></html>
    `)
		popup.document.close()
		popup.focus()
		popup.print()
		popup.close()
	} catch {
		toast.error('Gagal mencetak member card.', { timeout: 2000 })
	}
}
</script>