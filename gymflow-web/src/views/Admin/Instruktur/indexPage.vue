<template>
	<div class="page-header">
		<div>
			<h2 class="page-title">Instruktur</h2>
			<p class="page-subtitle">Kelola data instruktur gym</p>
		</div>
		<v-btn :to="{ name: 'admin.instruktur.create' }" rounded="lg" class="new-btn" prepend-icon="mdi-plus">
			Tambah Instruktur
		</v-btn>
	</div>

	<v-card class="table-card" rounded="xl">
		<v-card-text class="pa-6">
			<v-text-field v-model="search" prepend-inner-icon="mdi-magnify" placeholder="Cari nama instruktur..."
				variant="outlined" density="comfortable" color="orange" bg-color="#1a1a1a" class="mb-4"
				style="max-width: 400px;" hide-details />

			<div v-if="loading" class="d-flex justify-center pa-8">
				<v-progress-circular indeterminate color="orange" />
			</div>

			<div v-else class="table-wrapper">
				<table class="data-table">
					<thead>
						<tr>
							<th>Nama Instruktur</th>
							<th>Username</th>
							<th>Tanggal Lahir</th>
							<th>Aksi</th>
						</tr>
					</thead>
					<tbody>
						<tr v-if="filtered.length === 0">
							<td colspan="4" class="empty-row">Tidak ada data instruktur.</td>
						</tr>
						<tr v-for="item in filtered" :key="item.id_instruktur">
							<td class="font-weight-medium">{{ item.nama_instruktur }}</td>
							<td>{{ item.username ?? '—' }}</td>
							<td>{{ item.tanggal_lahir ?? '—' }}</td>
							<td>
								<div class="action-btns">
									<v-btn :to="{ name: 'admin.instruktur.edit', params: { id: item.id_instruktur } }"
										icon variant="text" color="blue" size="small">
										<v-icon size="18">mdi-pencil</v-icon>
									</v-btn>
									<v-btn icon variant="text" color="red" size="small" @click="confirmDelete(item)">
										<v-icon size="18">mdi-trash-can-outline</v-icon>
									</v-btn>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</v-card-text>
	</v-card>

	<!-- Delete dialog -->
	<v-dialog v-model="deleteDialog" max-width="400">
		<v-card rounded="xl" style="background: #111111; border: 1px solid rgba(255,255,255,0.06);">
			<v-card-text class="pa-6">
				<div class="d-flex align-center gap-3 mb-4">
					<div class="delete-icon-wrap"><v-icon color="red" size="24">mdi-alert-circle</v-icon></div>
					<div>
						<p class="dialog-title">Hapus Instruktur</p>
						<p class="dialog-subtitle">Tindakan ini tidak dapat dibatalkan.</p>
					</div>
				</div>
				<p class="text-sm" style="color: #9ca3af;">
					Yakin ingin menghapus <strong style="color: #f1f5f9;">{{ toDelete?.nama_instruktur }}</strong>?
				</p>
			</v-card-text>
			<v-card-actions class="px-6 pb-6 pt-0 gap-3">
				<v-spacer />
				<v-btn variant="outlined" color="grey" rounded="lg" @click="deleteDialog = false">Batal</v-btn>
				<v-btn color="red" variant="flat" rounded="lg" :loading="deleteLoading"
					@click="deleteItem('id_instruktur')">Hapus</v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script setup lang="ts">
import { useTable } from '@/composables/useTable'

const { items, search, loading, filtered, deleteDialog, deleteLoading, toDelete, confirmDelete, deleteItem } =
	useTable<any>('/instruktur', (item, s) => item.nama_instruktur?.toLowerCase().includes(s.toLowerCase()))
</script>