<template>
    <v-app :theme="'dark'" style="background: #0d0d0d;">

        <v-navigation-drawer v-model="drawer" :rail="rail" permanent
            style="background: #111111; border-right: 1px solid rgba(255,255,255,0.06);">
            <div class="logo-area" @click="rail = !rail">
                <div class="logo-icon">
                    <v-icon color="orange" size="20">mdi-dumbbell</v-icon>
                </div>
                <span v-if="!rail" class="logo-text">GymFlow</span>
                <v-spacer v-if="!rail" />
                <v-icon v-if="!rail" size="18" color="grey">mdi-chevron-left</v-icon>
            </div>

            <v-divider style="border-color: rgba(255,255,255,0.06);" />

            <div v-if="!rail" class="user-card">
                <div class="user-avatar">{{ userInitials }}</div>
                <div class="user-info">
                    <p class="user-name">{{ auth.user?.name ?? 'User' }}</p>
                    <p class="user-role">{{ roleLabel }}</p>
                </div>
            </div>
            <div v-if="!rail" style="padding: 0 12px 8px;">
                <v-divider style="border-color: rgba(255,255,255,0.06);" />
            </div>

            <div v-if="!rail && navGroups.length > 0">
                <template v-for="group in navGroups" :key="group.label">
                    <div class="nav-label">{{ group.label }}</div>
                    <v-list density="compact" nav style="padding: 0 8px;">
                        <v-list-item v-for="item in group.items" :key="item.name" :to="{ name: item.route }"
                            :value="item.name" active-color="orange" rounded="lg" class="nav-item"
                            style="margin-bottom: 2px;">
                            <template v-slot:prepend>
                                <v-icon :icon="item.icon" size="20" />
                            </template>
                            <v-list-item-title>{{ item.label }}</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </template>
            </div>

            <!-- Rail mode (icon only) -->
            <div v-if="rail">
                <v-list density="compact" nav style="padding: 0 8px;">
                    <v-list-item v-for="item in allNavItems" :key="item.name" :to="{ name: item.route }"
                        :value="item.name" active-color="orange" rounded="lg" class="nav-item"
                        style="margin-bottom: 2px;">
                        <template v-slot:prepend>
                            <v-icon :icon="item.icon" size="20" />
                        </template>
                    </v-list-item>
                </v-list>
            </div>

            <template v-slot:append>
                <v-divider style="border-color: rgba(255,255,255,0.06); margin-bottom: 8px;" />
                <div style="padding: 0 8px 12px;">
                    <v-list-item rounded="lg" class="logout-item" @click="logout">
                        <template v-slot:prepend>
                            <v-icon icon="mdi-logout" size="20" />
                        </template>
                        <v-list-item-title v-if="!rail">Log Out</v-list-item-title>
                    </v-list-item>
                </div>
            </template>
        </v-navigation-drawer>

        <v-app-bar flat style="background: rgba(13,13,13,0.9); backdrop-filter: blur(12px);
             border-bottom: 1px solid rgba(255,255,255,0.06);">
            <v-app-bar-nav-icon @click="drawer = !drawer" color="grey" class="d-flex d-md-none" />
            <v-app-bar-title>
                <span class="page-title">{{ currentPageTitle }}</span>
            </v-app-bar-title>
            <template v-slot:append>
                <div class="topbar-right">
                    <v-btn icon variant="text" color="grey" size="small">
                        <v-icon size="20">mdi-bell-outline</v-icon>
                    </v-btn>
                    <div class="topbar-avatar">
                        <span>{{ userInitials }}</span>
                    </div>
                </div>
            </template>
        </v-app-bar>

        <v-main style="background: #0d0d0d; min-height: 100vh;">
            <div class="content-wrapper">
                <router-view />
            </div>
        </v-main>

    </v-app>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useToast } from 'vue-toastification'
import api from '@/lib/axios'

interface NavItem {
    name: string
    label: string
    icon: string
    route: string
}

interface NavGroup {
    label: string
    items: NavItem[]
}

const props = defineProps<{
    navGroups: NavGroup[]
    roleLabel: string
}>()

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const toast = useToast()

const drawer = ref(true)
const rail = ref(false)

const allNavItems = computed(() =>
    props.navGroups.flatMap(g => g.items)
)

const userInitials = computed(() => {
    const name = auth.user?.name ?? 'U'
    return name.split(' ').map((n: string) => n[0]).join('').toUpperCase().slice(0, 2)
})

const currentPageTitle = computed(() => {
    const matched = allNavItems.value.find(item =>
        route.name?.toString().startsWith(item.name)
    )
    return matched?.label ?? 'Dashboard'
})

async function logout() {
    try {
        await api.post('/api/logout')
    } catch {
        // proceed even if token already invalid
    }
    auth.logout()
    toast.error('Berhasil Logout!', { timeout: 2000 })
    router.push({ name: 'login' })
}
</script>

<style scoped>
.logo-area {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 16px 14px;
    cursor: pointer;
    user-select: none;
}

.logo-icon {
    width: 34px;
    height: 34px;
    border-radius: 8px;
    background: rgba(251, 146, 60, 0.15);
    border: 1px solid rgba(251, 146, 60, 0.25);
    display: grid;
    place-items: center;
    flex-shrink: 0;
}

.logo-text {
    font-family: 'Montserrat', sans-serif;
    font-weight: 800;
    font-size: 1.1rem;
    color: #ffffff;
    letter-spacing: -0.3px;
}

.user-card {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 12px 14px;
}

.user-avatar {
    width: 34px;
    height: 34px;
    border-radius: 50%;
    background: rgba(249, 115, 22, 0.2);
    border: 1px solid rgba(249, 115, 22, 0.3);
    display: grid;
    place-items: center;
    font-size: 0.7rem;
    font-weight: 700;
    color: #f97316;
    flex-shrink: 0;
}

.user-info {
    flex: 1;
    min-width: 0;
}

.user-name {
    font-size: 0.85rem;
    font-weight: 600;
    color: #f1f5f9;
    margin: 0;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.user-role {
    font-size: 0.7rem;
    color: #f97316;
    margin: 0;
    font-weight: 500;
}

.nav-label {
    font-size: 0.65rem;
    font-weight: 600;
    color: #4b5563;
    text-transform: uppercase;
    letter-spacing: 0.08em;
    padding: 8px 20px 4px;
}

.nav-item {
    color: #9ca3af !important;
}

.logout-item {
    color: #6b7280 !important;
}

.logout-item:hover {
    color: #ef4444 !important;
    background: rgba(239, 68, 68, 0.08) !important;
}

.page-title {
    font-family: 'Montserrat', sans-serif;
    font-weight: 700;
    font-size: 1rem;
    color: #f1f5f9;
}

.topbar-right {
    display: flex;
    align-items: center;
    gap: 8px;
    padding-right: 12px;
}

.topbar-avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: rgba(249, 115, 22, 0.2);
    border: 1px solid rgba(249, 115, 22, 0.3);
    display: grid;
    place-items: center;
    font-size: 0.65rem;
    font-weight: 700;
    color: #f97316;
    cursor: pointer;
}

.content-wrapper {
    padding: 24px;
    max-width: 1400px;
}
</style>