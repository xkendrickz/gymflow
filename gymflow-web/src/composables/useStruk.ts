const STRUK_CSS = `
    body { font-family: 'Segoe UI', sans-serif; display: flex; justify-content: center; padding: 40px; background: #f5f5f5; }
    .card { background: white; border-radius: 12px; padding: 24px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
    .header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 16px; }
    .gym-name { font-size: 20px; font-weight: 800; color: #f97316; margin: 0 0 2px; }
    .address { font-size: 11px; color: #9ca3af; margin: 0; }
    .no-struk { font-size: 12px; color: #6b7280; text-align: right; }
    .divider { border: none; border-top: 1px solid #e5e7eb; margin: 12px 0; }
    .label { font-size: 10px; color: #9ca3af; text-transform: uppercase; letter-spacing: 0.05em; }
    .value { font-size: 13px; color: #111; font-weight: 500; margin-bottom: 8px; }
    .badge { display: inline-block; background: #f97316; color: white; font-size: 10px; padding: 2px 10px; border-radius: 99px; font-weight: 600; margin-bottom: 12px; }
    table { width: 100%; border-collapse: collapse; }
    th, td { border: 1px solid #e5e7eb; padding: 8px 12px; font-size: 13px; }
    th { background: #f9fafb; font-weight: 600; }
    .total-row td { font-weight: 700; background: #fff7ed; }
`

function openPopup(title: string, body: string) {
  const popup = window.open('', '_blank')
  if (!popup) return
  popup.document.write(
    `<html><head><title>${title}</title><style>${STRUK_CSS}</style></head><body>${body}</body></html>`,
  )
  popup.document.close()
  popup.focus()
  popup.print()
  popup.close()
}

const GYM_HEADER = `
    <p class="gym-name">GymFlow</p>
    <p class="address">Jl. Qlipoth Tree No. 5, Yogyakarta</p>
`

type StrukType = 'gym' | 'kelas' | 'aktivasi' | 'depositKelas' | 'laporan'

export function useStruk() {
  function printStruk(type: StrukType, data: any) {
    switch (type) {
      case 'gym':
        openPopup(
          'Struk Presensi Gym',
          `
                    <div class="card" style="width:320px">
                        ${GYM_HEADER}
                        <span class="badge">STRUK PRESENSI GYM</span>
                        <hr class="divider">
                        <p class="label">No Struk</p><p class="value">${data.no_struk}</p>
                        <p class="label">Tanggal</p><p class="value">${data.tanggal}</p>
                        <p class="label">Member</p><p class="value">${data.member_id} / ${data.nama_member}</p>
                        <p class="label">Slot Waktu</p><p class="value">${data.slot_waktu}</p>
                    </div>`,
        )
        break

      case 'kelas':
        openPopup(
          'Struk Presensi Kelas',
          `
                    <div class="card" style="width:320px">
                        ${GYM_HEADER}
                        <span class="badge">STRUK PRESENSI KELAS</span>
                        <hr class="divider">
                        <p class="label">No Struk</p><p class="value">${data.no_struk}</p>
                        <p class="label">Tanggal</p><p class="value">${data.tanggal}</p>
                        <p class="label">Member</p><p class="value">${data.member_id} / ${data.nama_member}</p>
                        <p class="label">Kelas</p><p class="value">${data.nama_kelas}</p>
                        <p class="label">Instruktur</p><p class="value">${data.nama_instruktur}</p>
                        ${
                          data.jenis === 'Reguler'
                            ? `<p class="label">Tarif</p><p class="value">Rp ${data.tarif}</p>
                               <p class="label">Sisa Deposit</p><p class="value">${data.sisa_deposit_reguler}</p>`
                            : `<p class="label">Sisa Deposit Paket</p><p class="value">${data.sisa_deposit_paket}x</p>
                               <p class="label">Berlaku Sampai</p><p class="value">${data.berlaku_sampai}</p>`
                        }
                    </div>`,
        )
        break

      case 'aktivasi':
        openPopup(
          'Struk Aktivasi',
          `
                    <div class="card" style="width:380px">
                        <div class="header">
                            <div>${GYM_HEADER}</div>
                            <div class="no-struk">No: ${data.no_struk}<br>${data.tanggal_aktivasi}</div>
                        </div>
                        <span class="badge">STRUK AKTIVASI</span>
                        <hr class="divider">
                        <p class="label">Member</p><p class="value">${data.member_id} / ${data.nama_member}</p>
                        <p class="label">Biaya Aktivasi</p><p class="value">Rp ${data.harga},-</p>
                        <p class="label">Masa Aktif Hingga</p><p class="value">${data.masa_aktif}</p>
                        <p class="label">Kasir</p><p class="value">P${data.id_pegawai} / ${data.nama_pegawai}</p>
                    </div>`,
        )
        break

      case 'depositKelas':
        openPopup(
          'Struk Deposit Kelas',
          `
                    <div class="card" style="width:380px">
                        <div class="header">
                            <div>${GYM_HEADER}</div>
                            <div class="no-struk">No: ${data.no_struk}<br>${data.tanggal}</div>
                        </div>
                        <span class="badge">STRUK DEPOSIT KELAS</span>
                        <hr class="divider">
                        <p class="label">Member</p><p class="value">${data.member_id} / ${data.nama_member}</p>
                        <p class="label">Kelas</p><p class="value">${data.nama_kelas}</p>
                        <p class="label">Deposit</p><p class="value">Rp ${data.harga},-</p>
                        <p class="label">Total Sesi</p><p class="value">${data.jumlah_deposit_paket} sesi</p>
                        <p class="label">Berlaku Sampai</p><p class="value">${data.berlaku_sampai}</p>
                        <p class="label">Kasir</p><p class="value">P${data.id_pegawai} / ${data.nama_pegawai}</p>
                    </div>`,
        )
        break
    }
  }

  function printLaporan(
    title: string,
    subtitle: string,
    headers: string[],
    rows: string[][],
    totals?: string[],
  ) {
    const headerRow = headers.map((h) => `<th>${h}</th>`).join('')
    const dataRows = rows.map((r) => `<tr>${r.map((c) => `<td>${c}</td>`).join('')}</tr>`).join('')
    const totalRow = totals
      ? `<tr class="total-row">${totals.map((t, i) => `<td${i === 0 ? ` colspan="${headers.length - totals.length + 1}" style="text-align:right"` : ''}>${t}</td>`).join('')}</tr>`
      : ''

    openPopup(
      title,
      `
            <div class="card" style="width:650px">
                ${GYM_HEADER}
                <h3 style="font-size:14px;text-decoration:underline;margin:16px 0 8px;">${subtitle}</h3>
                <table>
                    <tr>${headerRow}</tr>
                    ${dataRows}
                    ${totalRow}
                </table>
            </div>`,
    )
  }

  return { printStruk, printLaporan }
}
