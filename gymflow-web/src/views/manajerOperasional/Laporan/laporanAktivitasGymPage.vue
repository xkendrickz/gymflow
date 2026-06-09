<template>
	<div class="page-header">
		<h2 class="page-title">Laporan Aktivitas Gym</h2>
		<p class="page-subtitle">Aktivitas penggunaan gym bulanan</p>
	</div>

	<v-card class="form-card" rounded="xl">
		<v-card-text class="pa-6">
			<form @submit.prevent="cetak">
				<div class="form-grid">
					<div>
						<p class="field-label">Bulan</p>
						<v-select v-model="form.bulan" :items="dropdown.months" item-title="month" item-value="month"
							placeholder="Pilih bulan" variant="outlined" density="comfortable" color="orange"
							bg-color="#1a1a1a" />
					</div>
					<div>
						<p class="field-label">Tahun</p>
						<v-select v-model="form.tahun" :items="dropdown.years" item-title="year" item-value="year"
							placeholder="Pilih tahun" variant="outlined" density="comfortable" color="orange"
							bg-color="#1a1a1a" />
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
	'dropdownAktivitasGym',
	(f) => `/laporanAktivitasGym/${f.bulan}/${f.tahun}`,
	({ data, total, bulan, tahun, tanggal }, { printLaporan }) => {
		printLaporan(
			`LAPORAN AKTIVITAS GYM — ${bulan} ${tahun}`,
			`Tanggal cetak: ${tanggal}`,
			['Tanggal', 'Jumlah Member'],
			data.map((i: any) => [i.tanggal, i.jumlah_member]),
			['TOTAL', String(total)]
		)
	}
)
</script>