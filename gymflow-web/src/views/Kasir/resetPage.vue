<template>
	<div class="page-header">
		<h2 class="page-title">Reset & Expired</h2>
		<p class="page-subtitle">Kelola status kadaluarsa member dan instruktur</p>
	</div>

	<!-- Action buttons -->
	<div class="d-flex gap-3 mb-6">
		<v-btn :loading="loadingReset1" rounded="lg" class="reset-btn-danger" prepend-icon="mdi-refresh"
			@click="resetInstruktur">
			Reset Terlambat Instruktur
		</v-btn>
		<v-btn :loading="loadingReset2" rounded="lg" class="reset-btn-danger" prepend-icon="mdi-account-off"
			@click="resetMember">
			Deaktivasi Member & Reset Deposit
		</v-btn>
	</div>

	<div class="grid-2">
		<!-- Membercard Expired -->
		<v-card class="table-card" rounded="xl">
			<v-card-text class="pa-6">
				<p class="section-label">MEMBERCARD EXPIRED</p>
				<div v-if="loading" class="d-flex justify-center pa-4">
					<v-progress-circular indeterminate color="orange" size="24" />
				</div>
				<div v-else class="table-wrapper">
					<table class="data-table">
						<thead>
							<tr>
								<th>Nama Member</th>
								<th>Masa Aktif</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="aktivasi.length === 0">
								<td colspan="2" class="empty-row">Tidak ada data.</td>
							</tr>
							<tr v-for="(item, i) in aktivasi" :key="i">
								<td>{{ item.nama_member ?? '—' }}</td>
								<td><span class="expired-badge">{{ item.masa_aktif ?? '—' }}</span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</v-card-text>
		</v-card>

		<!-- Deposit Paket Expired -->
		<v-card class="table-card" rounded="xl">
			<v-card-text class="pa-6">
				<p class="section-label">DEPOSIT PAKET EXPIRED</p>
				<div v-if="loading" class="d-flex justify-center pa-4">
					<v-progress-circular indeterminate color="orange" size="24" />
				</div>
				<div v-else class="table-wrapper">
					<table class="data-table">
						<thead>
							<tr>
								<th>Nama Member</th>
								<th>Berlaku Sampai</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="deposit.length === 0">
								<td colspan="2" class="empty-row">Tidak ada data.</td>
							</tr>
							<tr v-for="(item, i) in deposit" :key="i">
								<td>{{ item.nama_member ?? '—' }}</td>
								<td><span class="expired-badge">{{ item.berlaku_sampai ?? '—' }}</span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</v-card-text>
		</v-card>
	</div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import api from '@/lib/axios'

const toast = useToast()
const aktivasi = ref<any[]>([])
const deposit = ref<any[]>([])
const loading = ref(true)
const loadingReset1 = ref(false)
const loadingReset2 = ref(false)

onMounted(async () => {
    try {
        const [r1, r2] = await Promise.all([
            api.get('/indexAktivasi'), api.get('/indexDeposit')
        ])
        aktivasi.value = r1.data.data
        deposit.value = r2.data.data
    } catch { toast.error('Gagal memuat data.', { timeout: 2000 }) }
    finally { loading.value = false }
})

async function runReset(endpoint: string, loadingRef: typeof loadingReset1, msg: string) {
    loadingRef.value = true
    try {
        await api.post(`/${endpoint}`)
        toast.success(msg, { timeout: 2000 })
    } catch { toast.error('Gagal melakukan reset.', { timeout: 2000 }) }
    finally { loadingRef.value = false }
}

const resetInstruktur = () => runReset('resetInstruktur', loadingReset1, 'Berhasil Reset Waktu Terlambat Instruktur!')
const resetMember = () => runReset('resetMember', loadingReset2, 'Berhasil Reset Status Member dan Sisa Deposit Paket!')
</script>