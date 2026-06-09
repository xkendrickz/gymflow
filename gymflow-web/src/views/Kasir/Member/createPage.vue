<template>
    <div class="page-header">
        <h2 class="page-title">New Member</h2>
        <p class="page-subtitle">Register a new gym member</p>
    </div>

    <v-card class="form-card" rounded="xl">
        <v-card-text class="pa-6">
            <form @submit.prevent="store">
                <div class="form-grid">

                    <div>
                        <p class="field-label">Nama Member</p>
                        <v-text-field v-model="form.nama_member" placeholder="Masukkan nama member" variant="outlined"
                            density="comfortable" color="orange" bg-color="#1a1a1a"
                            :error-messages="errors.nama_member" />
                    </div>

                    <div>
                        <p class="field-label">Email</p>
                        <v-text-field v-model="form.email" type="email" placeholder="Masukkan email" variant="outlined"
                            density="comfortable" color="orange" bg-color="#1a1a1a" :error-messages="errors.email" />
                    </div>

                    <div>
                        <p class="field-label">Telepon</p>
                        <v-text-field v-model="form.telepon" placeholder="Masukkan nomor telepon" variant="outlined"
                            density="comfortable" color="orange" bg-color="#1a1a1a" :error-messages="errors.telepon" />
                    </div>

                    <div>
                        <p class="field-label">Alamat</p>
                        <v-text-field v-model="form.alamat" placeholder="Masukkan alamat" variant="outlined"
                            density="comfortable" color="orange" bg-color="#1a1a1a" :error-messages="errors.alamat" />
                    </div>

                    <div>
                        <p class="field-label">Status</p>
                        <v-select v-model="form.status" :items="['aktif', 'tidak aktif']" placeholder="Pilih status"
                            variant="outlined" density="comfortable" color="orange" bg-color="#1a1a1a"
                            :error-messages="errors.status" />
                    </div>

                    <div>
                        <p class="field-label">Tanggal Lahir</p>
                        <v-text-field v-model="form.tanggal_lahir" type="date" variant="outlined" density="comfortable"
                            color="orange" bg-color="#1a1a1a" :error-messages="errors.tanggal_lahir" />
                    </div>

                    <div>
                        <p class="field-label">Tanggal Pendaftaran</p>
                        <v-text-field v-model="form.tanggal_daftar" type="date" variant="outlined" density="comfortable"
                            color="orange" bg-color="#1a1a1a" :error-messages="errors.tanggal_daftar" />
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
                        @click="router.push({ name: 'kasir.member.index' })">
                        Batal
                    </v-btn>
                    <v-btn type="submit" :loading="loading" rounded="lg" class="save-btn">
                        <v-icon start>mdi-content-save</v-icon>
                        Simpan
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

const { form, errors, loading, submit } = useForm({
    nama_member: '',
    email: '',
    telepon: '',
    alamat: '',
    status: '',
    tanggal_lahir: '',
    tanggal_daftar: '',
    username: '',
    password: '',
})

const store = () =>
    submit('post', '/member', 'Berhasil Register Member!', 'kasir.member.index')
</script>