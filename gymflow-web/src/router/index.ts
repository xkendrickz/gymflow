import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

declare module 'vue-router' {
  interface RouteMeta {
    requiresAuth?: boolean
    role?: 'admin' | 'kasir' | 'member' // remove 'mo'
  }
}

const routes = [
  {
    path: '/',
    name: 'login',
    component: () => import('@/components/LoginPage.vue'),
  },

  {
    path: '/admin',
    name: 'admin.beranda',
    component: () => import('@/components/AdminDashboard.vue'),
    meta: { requiresAuth: true, role: 'admin' as const },
    children: [
      {
        path: 'pegawai',
        name: 'admin.pegawai.index',
        component: () => import('@/views/Admin/Pegawai/indexPage.vue'),
      },
      {
        path: 'pegawai/create',
        name: 'admin.pegawai.create',
        component: () => import('@/views/Admin/Pegawai/createPage.vue'),
      },
      {
        path: 'pegawai/edit/:id',
        name: 'admin.pegawai.edit',
        component: () => import('@/views/Admin/Pegawai/editPage.vue'),
      },
      {
        path: 'instruktur',
        name: 'admin.instruktur.index',
        component: () => import('@/views/Admin/Instruktur/indexPage.vue'),
      },
      {
        path: 'instruktur/create',
        name: 'admin.instruktur.create',
        component: () => import('@/views/Admin/Instruktur/createPage.vue'),
      },
      {
        path: 'instruktur/edit/:id',
        name: 'admin.instruktur.edit',
        component: () => import('@/views/Admin/Instruktur/editPage.vue'),
      },
      {
        path: 'kelas',
        name: 'admin.kelas.index',
        component: () => import('@/views/Admin/Kelas/indexPage.vue'),
      },
      {
        path: 'kelas/create',
        name: 'admin.kelas.create',
        component: () => import('@/views/Admin/Kelas/createPage.vue'),
      },
      {
        path: 'kelas/edit/:id',
        name: 'admin.kelas.edit',
        component: () => import('@/views/Admin/Kelas/editPage.vue'),
      },
      {
        path: 'jadwalUmum',
        name: 'admin.jadwalUmum.index',
        component: () => import('@/views/manajerOperasional/JadwalUmum/indexPage.vue'),
      },
      {
        path: 'jadwalUmum/create',
        name: 'admin.jadwalUmum.create',
        component: () => import('@/views/manajerOperasional/JadwalUmum/createPage.vue'),
      },
      {
        path: 'jadwalUmum/edit/:id',
        name: 'admin.jadwalUmum.edit',
        component: () => import('@/views/manajerOperasional/JadwalUmum/editPage.vue'),
      },
      {
        path: 'jadwalHarian',
        name: 'admin.jadwalHarian.index',
        component: () => import('@/views/manajerOperasional/JadwalHarian/indexPage.vue'),
      },
      {
        path: 'jadwalHarian/edit/:id',
        name: 'admin.jadwalHarian.edit',
        component: () => import('@/views/manajerOperasional/JadwalHarian/editPage.vue'),
      },
      {
        path: 'izin',
        name: 'admin.izin.index',
        component: () => import('@/views/manajerOperasional/Izin/indexPage.vue'),
      },
      {
        path: 'laporan/pendapatan',
        name: 'admin.laporan.pendapatan',
        component: () => import('@/views/manajerOperasional/Laporan/laporanPendapatanPage.vue'),
      },
      {
        path: 'laporan/aktivitasKelas',
        name: 'admin.laporan.aktivitasKelas',
        component: () => import('@/views/manajerOperasional/Laporan/laporanAktivitasKelasPage.vue'),
      },
      {
        path: 'laporan/aktivitasGym',
        name: 'admin.laporan.aktivitasGym',
        component: () => import('@/views/manajerOperasional/Laporan/laporanAktivitasGymPage.vue'),
      },
      {
        path: 'laporan/kinerja',
        name: 'admin.laporan.kinerja',
        component: () =>
          import('@/views/manajerOperasional/Laporan/laporanKinerjaInstrukturPage.vue'),
      },
    ],
  },

  {
    path: '/kasir',
    name: 'kasir.beranda',
    component: () => import('@/components/KasirDashboard.vue'),
    meta: { requiresAuth: true, role: 'kasir' as const },
    children: [
      {
        path: 'member',
        name: 'kasir.member.index',
        component: () => import('@/views/Kasir/Member/indexPage.vue'),
      },
      {
        path: 'member/create',
        name: 'kasir.member.create',
        component: () => import('@/views/Kasir/Member/createPage.vue'),
      },
      {
        path: 'member/edit/:id',
        name: 'kasir.member.edit',
        component: () => import('@/views/Kasir/Member/editPage.vue'),
      },
      {
        path: 'aktivasi',
        name: 'kasir.aktivasi',
        component: () => import('@/views/Kasir/Transaksi/aktivasiPage.vue'),
      },
      {
        path: 'depositReguler',
        name: 'kasir.depositreguler',
        component: () => import('@/views/Kasir/Transaksi/depositRegulerPage.vue'),
      },
      {
        path: 'depositKelas',
        name: 'kasir.depositkelas',
        component: () => import('@/views/Kasir/Transaksi/depositKelasPage.vue'),
      },
      {
        path: 'reset',
        name: 'kasir.reset',
        component: () => import('@/views/Kasir/resetPage.vue'),
      },
      {
        path: 'presensi/gym',
        name: 'kasir.presensiGym',
        component: () => import('@/views/Kasir/Presensi/PresensiGym.vue'),
      },
      {
        path: 'presensi/kelas',
        name: 'kasir.presensiKelas',
        component: () => import('@/views/Kasir/Presensi/PresensiKelas.vue'),
      },
    ],
  },

  // 404
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('@/views/NotFoundPage.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()

  if (to.meta.requiresAuth && !auth.isLoggedIn) {
    next({ name: 'login' })
  } else if (to.meta.role && auth.role !== to.meta.role) {
    next({ name: 'login' })
  } else {
    next()
  }
})

export default router
