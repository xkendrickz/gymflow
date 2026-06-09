<template>
	<div class="page-header">
		<h2 class="page-title">Laporan Aktivitas Kelas</h2>
		<p class="page-subtitle">Aktivitas penggunaan kelas bulanan</p>
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
	'dropdownAktivitasKelas',
	(f) => `/laporanAktivitasKelas/${f.bulan}/${f.tahun}`,
	({ data, bulan, tahun, tanggal }, { printLaporan }) => {
		printLaporan(
			`LAPORAN AKTIVITAS KELAS — ${bulan} ${tahun}`,
			`Tanggal cetak: ${tanggal}`,
			['Kelas', 'Instruktur', 'Jumlah Peserta', 'Jumlah Libur'],
			data.map((i: any) => [i.nama_kelas, i.nama_instruktur, i.total_peserta, i.total_libur])
		)
	}
)
</script>