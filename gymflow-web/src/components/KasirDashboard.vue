<template>
	<DashboardLayout :nav-groups="navGroups" role-label="Kasir" />
</template>

<script setup lang="ts">
import DashboardLayout from '@/components/DashboardLayout.vue'

const navGroups = [
	{
		label: 'Member',
		items: [
			{ name: 'kasir.member', label: 'Member', icon: 'mdi-account-group', route: 'kasir.member.index' },
		],
	},
	{
		label: 'Transaksi',
		items: [
			{ name: 'kasir.aktivasi', label: 'Aktivasi', icon: 'mdi-card-account-details', route: 'kasir.aktivasi' },
			{ name: 'kasir.depositreguler', label: 'Deposit Reguler', icon: 'mdi-cash-plus', route: 'kasir.depositreguler' },
			{ name: 'kasir.depositkelas', label: 'Deposit Kelas', icon: 'mdi-cash-multiple', route: 'kasir.depositkelas' },
		],
	},
	{
		label: 'Presensi',
		items: [
			{ name: 'kasir.presensiGym', label: 'Presensi Gym', icon: 'mdi-dumbbell', route: 'kasir.presensiGym' },
			{ name: 'kasir.presensiKelas', label: 'Presensi Kelas', icon: 'mdi-human-handsup', route: 'kasir.presensiKelas' },
		],
	},
	{
		label: 'Lainnya',
		items: [
			{ name: 'kasir.reset', label: 'Reset', icon: 'mdi-refresh', route: 'kasir.reset' },
		],
	},
]
</script>