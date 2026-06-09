<template>
	<div class="page-header">
		<div>
			<h2 class="page-title">Jadwal Harian</h2>
			<p class="page-subtitle">Jadwal kelas minggu ini</p>
		</div>
		<div class="d-flex align-center gap-3">
			<v-text-field v-model="search" prepend-inner-icon="mdi-magnify" placeholder="Cari kelas..."
				variant="outlined" density="compact" color="orange" bg-color="#1a1a1a" hide-details
				style="max-width: 240px;" />
			<v-btn :loading="generating" :disabled="generateDisabled" rounded="lg" class="generate-btn"
				prepend-icon="mdi-refresh" @click="generate">
				Generate
			</v-btn>
		</div>
	</div>

	<div v-if="loading" class="d-flex justify-center pa-8">
		<v-progress-circular indeterminate color="orange" />
	</div>

	<template v-else>
		<v-card class="table-card mb-4" rounded="xl">
			<v-card-text class="pa-6">
				<p class="section-label">🌅 MORNING CLASS</p>
				<div class="table-wrapper">
					<table class="data-table">
						<thead>
							<tr>
								<th>Tanggal</th>
								<th>Jam</th>
								<th>Kelas</th>
								<th>Instruktur</th>
								<th>Status</th>
								<th>Aksi</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="morningGroups.length === 0">
								<td colspan="6" class="empty-row">Tidak ada jadwal.</td>
							</tr>
							<tr v-for="group in morningGroups" :key="group[0].id_jadwal_harian">
								<td>{{ formatDay(group[0].hari) }}</td>
								<td>{{ group[0].jam }}</td>
								<td>{{ group[0].nama_kelas }}</td>
								<td>{{ group[0].nama_instruktur }}</td>
								<td>
									<span v-if="group[0].status"
										:class="['status-badge', group[0].status === 'Libur' ? 'status-libur' : 'status-diganti']">
										{{ group[0].status }}
									</span>
									<span v-else class="status-badge status-active">Aktif</span>
								</td>
								<td>
									<v-btn
										:to="{ name: 'mo.jadwalHarian.edit', params: { id: group[0].id_jadwal_harian } }"
										icon variant="text" color="blue" size="small">
										<v-icon size="18">mdi-pencil</v-icon>
									</v-btn>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</v-card-text>
		</v-card>

		<v-card class="table-card" rounded="xl">
			<v-card-text class="pa-6">
				<p class="section-label">🌆 EVENING CLASS</p>
				<div class="table-wrapper">
					<table class="data-table">
						<thead>
							<tr>
								<th>Tanggal</th>
								<th>Jam</th>
								<th>Kelas</th>
								<th>Instruktur</th>
								<th>Status</th>
								<th>Aksi</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="eveningGroups.length === 0">
								<td colspan="6" class="empty-row">Tidak ada jadwal.</td>
							</tr>
							<tr v-for="group in eveningGroups" :key="group[0].id_jadwal_harian">
								<td>{{ formatDay(group[0].hari) }}</td>
								<td>{{ group[0].jam }}</td>
								<td>{{ group[0].nama_kelas }}</td>
								<td>{{ group[0].nama_instruktur }}</td>
								<td>
									<span v-if="group[0].status"
										:class="['status-badge', group[0].status === 'Libur' ? 'status-libur' : 'status-diganti']">
										{{ group[0].status }}
									</span>
									<span v-else class="status-badge status-active">Aktif</span>
								</td>
								<td>
									<v-btn
										:to="{ name: 'mo.jadwalHarian.edit', params: { id: group[0].id_jadwal_harian } }"
										icon variant="text" color="blue" size="small">
										<v-icon size="18">mdi-pencil</v-icon>
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
const jadwal = ref<any[]>([])
const search = ref('')
const loading = ref(true)
const generating = ref(false)

const ONE_WEEK_MS = 7 * 24 * 60 * 60 * 1000
const lastAccess = localStorage.getItem('jadwalHarianLastAccess')
const generateDisabled = ref(lastAccess
	? (Date.now() - new Date(lastAccess).getTime()) < ONE_WEEK_MS
	: false
)

function groupBy(data: any[], searchVal: string) {
	const groups: Record<string, any[]> = {}
	data.filter(i => i.nama_kelas?.toLowerCase().includes(searchVal.toLowerCase()))
		.forEach(item => {
			const key = `${item.hari}-${item.nama_kelas}`
			if (!groups[key]) groups[key] = []
			groups[key].push(item)
		})
	return Object.values(groups)
}

const morningGroups = computed(() =>
	groupBy(jadwal.value.filter(i => i.jam >= '00:00' && i.jam < '12:00'), search.value)
)
const eveningGroups = computed(() =>
	groupBy(jadwal.value.filter(i => i.jam >= '12:00' && i.jam <= '23:59'), search.value)
)

const formatDay = (date: string) =>
	new Date(date).toLocaleDateString('id-ID', { weekday: 'short', month: 'short', day: 'numeric' }).toUpperCase()

async function load() {
	const res = await api.get('/jadwalHarian')
	jadwal.value = res.data.data
}

onMounted(async () => {
	try { await load() }
	catch { toast.error('Gagal memuat data.', { timeout: 2000 }) }
	finally { loading.value = false }
})

async function generate() {
	generating.value = true
	try {
		await api.post('/jadwalHarian')
		localStorage.setItem('jadwalHarianLastAccess', new Date().toString())
		generateDisabled.value = true
		await load()
		toast.success('Berhasil Generate Jadwal Harian!', { timeout: 2000 })
	} catch { toast.error('Gagal generate jadwal.', { timeout: 2000 }) }
	finally { generating.value = false }
}
</script>