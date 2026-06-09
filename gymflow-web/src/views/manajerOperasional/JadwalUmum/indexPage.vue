<template>
	<div class="page-header">
		<div>
			<h2 class="page-title">Jadwal Umum</h2>
			<p class="page-subtitle">Kelola jadwal kelas mingguan</p>
		</div>
		<v-btn :to="{ name: 'mo.jadwalUmum.create' }" rounded="lg" class="new-btn" prepend-icon="mdi-plus">
			Tambah Jadwal
		</v-btn>
	</div>

	<div v-if="loading" class="d-flex justify-center pa-8">
		<v-progress-circular indeterminate color="orange" />
	</div>

	<template v-else>
		<v-card class="table-card mb-4" rounded="xl">
			<v-card-text class="pa-6">
				<p class="section-label">🌅 MORNING CLASS (00:00 - 11:59)</p>
				<ScheduleTable :groups="morningGroups" @delete="confirmDelete" />
			</v-card-text>
		</v-card>

		<v-card class="table-card" rounded="xl">
			<v-card-text class="pa-6">
				<p class="section-label">🌆 EVENING CLASS (12:00 - 23:59)</p>
				<ScheduleTable :groups="eveningGroups" @delete="confirmDelete" />
			</v-card-text>
		</v-card>
	</template>

	<v-dialog v-model="deleteDialog" max-width="400">
		<v-card rounded="xl" style="background: #111111; border: 1px solid rgba(255,255,255,0.06);">
			<v-card-text class="pa-6">
				<p style="color: #f1f5f9; font-weight: 700; margin-bottom: 8px;">Hapus Jadwal?</p>
				<p style="color: #9ca3af; font-size: 0.875rem;">Tindakan ini tidak dapat dibatalkan.</p>
			</v-card-text>
			<v-card-actions class="px-6 pb-6 pt-0">
				<v-spacer />
				<v-btn variant="outlined" color="grey" rounded="lg" @click="deleteDialog = false">Batal</v-btn>
				<v-btn color="red" variant="flat" rounded="lg" :loading="deleteLoading"
					@click="deleteJadwal">Hapus</v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, defineComponent, h } from 'vue'
import { useToast } from 'vue-toastification'
import api from '@/lib/axios'

const toast = useToast()
const jadwal = ref<any[]>([])
const loading = ref(true)
const deleteDialog = ref(false)
const deleteLoading = ref(false)
const toDeleteId = ref<number | null>(null)

function groupBy(data: any[]) {
	const groups: Record<string, any[]> = {}
	data.forEach(item => {
		const key = `${item.hari}-${item.nama_kelas}`
		if (!groups[key]) groups[key] = []
		groups[key].push(item)
	})
	return Object.values(groups)
}

const morningGroups = computed(() => groupBy(jadwal.value.filter(i => i.jam < '12:00')))
const eveningGroups = computed(() => groupBy(jadwal.value.filter(i => i.jam >= '12:00')))

const formatDay = (date: string) =>
	new Date(date).toLocaleDateString('id-ID', { weekday: 'long' }).toUpperCase()

onMounted(async () => {
	try {
		const res = await api.get('/jadwalUmum')
		jadwal.value = res.data.data
	} catch { toast.error('Gagal memuat data.', { timeout: 2000 }) }
	finally { loading.value = false }
})

function confirmDelete(id: number) { toDeleteId.value = id; deleteDialog.value = true }

async function deleteJadwal() {
	if (!toDeleteId.value) return
	deleteLoading.value = true
	try {
		await api.delete(`/jadwalUmum/${toDeleteId.value}`)
		jadwal.value = jadwal.value.filter(j => j.id_jadwal_umum !== toDeleteId.value)
		toast.success('Berhasil Hapus Jadwal!', { timeout: 2000 })
		deleteDialog.value = false
	} catch { toast.error('Gagal menghapus jadwal.', { timeout: 2000 }) }
	finally { deleteLoading.value = false }
}

const ScheduleTable = defineComponent({
	props: { groups: Array },
	emits: ['delete'],
	setup(props, { emit }) {
		return () => h('div', { class: 'table-wrapper' },
			h('table', { class: 'data-table' }, [
				h('thead', h('tr', [
					h('th', 'Hari'), h('th', 'Jam'), h('th', 'Kelas'), h('th', 'Instruktur'), h('th', 'Aksi')
				])),
				h('tbody', (props.groups as any[]).length === 0
					? [h('tr', h('td', { colspan: 5, class: 'empty-row' }, 'Tidak ada jadwal.'))]
					: (props.groups as any[]).map(group =>
						h('tr', { key: group[0].id_jadwal_umum }, [
							h('td', formatDay(group[0].hari)),
							h('td', group[0].jam),
							h('td', group[0].nama_kelas),
							h('td', group[0].nama_instruktur),
							h('td', h('div', { class: 'action-btns' }, [
								h('v-btn', {
									to: { name: 'mo.jadwalUmum.edit', params: { id: group[0].id_jadwal_umum } },
									icon: true, variant: 'text', color: 'blue', size: 'small'
								}, h('v-icon', { size: 18 }, 'mdi-pencil')),
								h('v-btn', {
									icon: true, variant: 'text', color: 'red', size: 'small',
									onClick: () => emit('delete', group[0].id_jadwal_umum)
								}, h('v-icon', { size: 18 }, 'mdi-trash-can-outline'))
							]))
						])
					)
				)
			])
		)
	}
})
</script>