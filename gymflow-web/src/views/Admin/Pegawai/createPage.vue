<template>
    <div class="page-header">
        <h2 class="page-title">Tambah Akun</h2>
        <p class="page-subtitle">Buat akun admin atau kasir baru</p>
    </div>

    <v-card class="form-card" rounded="xl">
        <v-card-text class="pa-6">
            <form @submit.prevent="store">
                <div class="form-grid">
                    <div>
                        <p class="field-label">Nama Pegawai</p>
                        <v-text-field v-model="form.nama_pegawai" placeholder="Masukkan nama pegawai" variant="outlined"
                            density="comfortable" color="orange" bg-color="#1a1a1a"
                            :error-messages="errors.nama_pegawai" />
                    </div>
                    <div>
                        <p class="field-label">Tanggal Lahir</p>
                        <v-text-field v-model="form.tanggal_lahir" type="date" variant="outlined" density="comfortable"
                            color="orange" bg-color="#1a1a1a" :error-messages="errors.tanggal_lahir" />
                    </div>
                    <div>
                        <p class="field-label">Role</p>
                        <v-select v-model="form.id_role" :items="roleOptions" item-title="label" item-value="value"
                            variant="outlined" density="comfortable" color="orange" bg-color="#1a1a1a"
                            :error-messages="errors.id_role" />
                    </div>
                    <div>
                        <p class="field-label">Username</p>
                        <v-text-field v-model="form.username" placeholder="Masukkan username" variant="outlined"
                            density="comfortable" color="orange" bg-color="#1a1a1a" :error-messages="errors.username" />
                    </div>
                    <div>
                        <p class="field-label">Password</p>
                        <v-text-field v-model="form.password" :type="showPassword ? 'text' : 'password'"
                            placeholder="Masukkan password" variant="outlined" density="comfortable" color="orange"
                            bg-color="#1a1a1a" :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                            @click:append-inner="showPassword = !showPassword" :error-messages="errors.password" />
                    </div>
                </div>
                <v-divider class="my-6" style="border-color: rgba(255,255,255,0.06);" />
                <div class="form-actions">
                    <v-btn variant="outlined" color="grey" rounded="lg"
                        @click="router.push({ name: 'admin.pegawai.index' })">Batal</v-btn>
                    <v-btn type="submit" :loading="loading" rounded="lg" class="save-btn">
                        <v-icon start>mdi-content-save</v-icon>Simpan
                    </v-btn>
                </div>
            </form>
        </v-card-text>
    </v-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useForm } from '@/composables/useForm'

const router = useRouter()
const showPassword = ref(false)
const roleOptions = [
    { label: 'Admin', value: 1 },
    { label: 'Kasir', value: 3 },
]

const { form, errors, loading, submit } = useForm({
    nama_pegawai: '',
    tanggal_lahir: '',
    id_role: 3,
    username: '',
    password: '',
})

const store = () =>
    submit('post', '/pegawai', 'Berhasil Tambah Akun!', 'admin.pegawai.index')
</script>