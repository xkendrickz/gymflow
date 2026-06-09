<template>
	<div class="page-header">
		<h2 class="page-title">Laporan Pendapatan</h2>
		<p class="page-subtitle">Laporan pendapatan tahunan</p>
	</div>

	<v-card class="form-card" rounded="xl">
		<v-card-text class="pa-6">
			<form @submit.prevent="cetak">
				<div class="form-grid">
					<div>
						<p class="field-label">Tahun</p>
						<v-select v-model="form.tahun" :items="dropdown" placeholder="Pilih tahun" variant="outlined"
							density="comfortable" color="orange" bg-color="#1a1a1a" />
					</div>
				</div>
				<v-divider class="my-6" style="border-color: rgba(255,255,255,0.06);" />
				<div class="form-actions">
					<v-btn type="submit" :loading="loading" rounded="lg" class="cetak-btn" prepend-icon="mdi-printer">
						Cetak Laporan
					</v-btn>
				</div>
			</form>
		</v-card-text>
	</v-card>
</template>

<script setup lang="ts">
import { useLaporan } from '@/composables/useLaporan'

const { form, dropdown, loading, cetak } = useLaporan(
	'dropdownPendapatan',
	(f) => `/laporanPendapatan/${f.tahun}`,
	({ data, total_tahunan, tahun, tanggal }, { printLaporan }) => {
		printLaporan(
			`LAPORAN PENDAPATAN — ${tahun}`,
			`Tanggal cetak: ${tanggal}`,
			['Bulan', 'Aktivasi', 'Deposit', 'Total'],
			data.map((i: any) => [i.nama_bulan, i.total_aktivasi, i.total_deposit, i.total_bulanan]),
			['TOTAL', String(total_tahunan)]
		)
	},
	false  // ← no bulan needed
)
</script>