<template>
    <DashboardLayout :nav-groups="navGroups" role-label="Admin" />
</template>

<script setup lang="ts">
import DashboardLayout from '@/components/DashboardLayout.vue'

const navGroups = [
    {
        label: 'SDM',
        items: [
            { name: 'admin.pegawai', label: 'Akun Pegawai', icon: 'mdi-account-key', route: 'admin.pegawai.index' },
            { name: 'admin.instruktur', label: 'Instruktur', icon: 'mdi-account-tie', route: 'admin.instruktur.index' },
            { name: 'admin.kelas', label: 'Kelas', icon: 'mdi-yoga', route: 'admin.kelas.index' },
        ],
    },
    {
        label: 'Jadwal',
        items: [
            { name: 'admin.jadwalUmum', label: 'Jadwal Umum', icon: 'mdi-calendar-clock', route: 'admin.jadwalUmum.index' },
            { name: 'admin.jadwalHarian', label: 'Jadwal Harian', icon: 'mdi-calendar-today', route: 'admin.jadwalHarian.index' },
            { name: 'admin.izin', label: 'Izin', icon: 'mdi-account-clock', route: 'admin.izin.index' },
        ],
    },
    {
        label: 'Laporan',
        items: [
            { name: 'admin.laporan.pendapatan', label: 'Pendapatan', icon: 'mdi-chart-line', route: 'admin.laporan.pendapatan' },
            { name: 'admin.laporan.aktivitasKelas', label: 'Aktivitas Kelas', icon: 'mdi-human-handsup', route: 'admin.laporan.aktivitasKelas' },
            { name: 'admin.laporan.aktivitasGym', label: 'Aktivitas Gym', icon: 'mdi-dumbbell', route: 'admin.laporan.aktivitasGym' },
            { name: 'admin.laporan.kinerja', label: 'Kinerja Instruktur', icon: 'mdi-account-star', route: 'admin.laporan.kinerja' },
        ],
    },
]
</script>