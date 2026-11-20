// ==========================================================================
// Marian Hostel Management System - Monthly Fee Tracking Module
// ==========================================================================

const fees = {
  data: [],

  async render() {
    const container = document.getElementById('viewFees');
    container.style.display = 'block';

    const res = await app.api('/fees');
    this.data = res.feeRecords || [];

    const isWarden = app.currentUser.role === 'WARDEN';

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div>
            <div class="card-title">
              <i class="fa-solid fa-file-invoice-dollar" style="color:var(--primary);"></i>
              Hostel Fee Management & Monthly Dues Tracking
            </div>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              ${isWarden ? 'Administrative ledger: review collection, edit dues, and record offline payments.' : 'View monthly rent, mess, and utility dues, make simulated payments, and get receipts.'}
            </p>
          </div>
        </div>

        <!-- Summary Banner -->
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:16px; margin-bottom: 24px;">
          <div style="background:#ecfdf5; border:1px solid #a7f3d0; padding:16px; border-radius:var(--radius-md);">
            <div style="font-size:0.75rem; text-transform:uppercase; color:#065f46; font-weight:700;">Total Paid Amount</div>
            <div style="font-size:1.4rem; font-weight:800; color:#059669; margin-top:4px;">₹${res.totalPaid || 0}</div>
            <span style="font-size:0.75rem; color:#065f46;">${res.paidCount || 0} Records Settled</span>
          </div>

          <div style="background:#fffbeb; border:1px solid #fde68a; padding:16px; border-radius:var(--radius-md);">
            <div style="font-size:0.75rem; text-transform:uppercase; color:#92400e; font-weight:700;">Pending Dues</div>
            <div style="font-size:1.4rem; font-weight:800; color:#d97706; margin-top:4px;">₹${res.totalPending || 0}</div>
            <span style="font-size:0.75rem; color:#92400e;">${res.pendingCount || 0} Records Pending</span>
          </div>
        </div>

        <!-- Monthly Fee Cards Grid -->
        <h3 style="font-size:1.1rem; font-weight:700; color:var(--secondary); margin-bottom: 16px;">
          <i class="fa-solid fa-receipt" style="color:var(--primary);"></i> Monthly Fee Breakdown
        </h3>

        ${this.data.length === 0 ? '<p style="color:var(--text-muted); text-align:center; padding:30px;">No fee records found.</p>' : ''}

        <div class="fee-grid">
          ${this.data.map(f => this.renderFeeCard(f, isWarden)).join('')}
        </div>
      </div>
    `;
  },

  renderFeeCard(f, isWarden) {
    const isPaid = f.status === 'PAID';
    const isPending = f.status === 'PENDING';
    const cls = isPaid ? 'paid' : (isPending ? 'pending' : 'overdue');

    return `
      <div class="fee-card ${cls}">
        <div>
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;">
            <div style="font-size:1.1rem; font-weight:700; color:var(--secondary);">${f.month} ${f.year}</div>
            <span class="badge ${isPaid ? 'badge-success' : 'badge-warning'}">${f.status}</span>
          </div>

          ${isWarden ? `
            <div style="font-size:0.85rem; color:var(--primary); font-weight:600; margin-bottom:8px;">
              <i class="fa-solid fa-user"></i> ${f.studentName} (Room ${f.studentRoom || 'N/A'})
            </div>
          ` : ''}

          <div style="background:#f8fafc; padding:12px; border-radius:var(--radius-sm); font-size:0.82rem; margin-bottom:12px; line-height:1.6;">
            <div style="display:flex; justify-content:space-between;">
              <span>Room Rent:</span> <strong>₹${f.rentAmount}</strong>
            </div>
            <div style="display:flex; justify-content:space-between;">
              <span>Mess Charges:</span> <strong>₹${f.messAmount}</strong>
            </div>
            <div style="display:flex; justify-content:space-between;">
              <span>Electricity & Maintenance:</span> <strong>₹${f.electricityAmount}</strong>
            </div>
            <div style="display:flex; justify-content:space-between; border-top:1px solid #e2e8f0; margin-top:6px; padding-top:6px; font-size:0.95rem; font-weight:800; color:#1e293b;">
              <span>Total Bill:</span> <span>₹${f.totalAmount}</span>
            </div>
          </div>

          ${isPaid ? `
            <div style="font-size:0.75rem; color:#475569; margin-bottom:12px;">
              <div><strong>Paid Date:</strong> ${f.paymentDate}</div>
              <div><strong>Txn ID:</strong> ${f.transactionId}</div>
              <div><strong>Mode:</strong> ${f.paymentMode}</div>
            </div>
          ` : ''}
        </div>

        <div>
          ${!isPaid && !isWarden ? `
            <button class="btn btn-primary btn-sm" style="width:100%;" onclick="fees.openPayModal('${f.id}', ${f.totalAmount}, '${f.month}')">
              <i class="fa-solid fa-credit-card"></i> Pay Fee (₹${f.totalAmount})
            </button>
          ` : ''}

          ${isPaid ? `
            <button class="btn btn-outline btn-sm" style="width:100%;" onclick="fees.viewReceiptModal('${f.id}')">
              <i class="fa-solid fa-file-invoice"></i> View Official Receipt
            </button>
          ` : ''}

          ${isWarden ? `
            <button class="btn btn-secondary btn-sm" style="width:100%; margin-top:6px;" onclick="fees.openWardenEditModal('${f.id}')">
              <i class="fa-solid fa-pen-to-square"></i> Edit Record (Warden)
            </button>
          ` : ''}
        </div>
      </div>
    `;
  },

  openPayModal(feeId, amount, month) {
    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:12px;">
        <i class="fa-solid fa-wallet" style="color:var(--primary);"></i> Pay Hostel Fee • ${month}
      </h3>
      <div style="background:#eff6ff; border:1px solid #bfdbfe; padding:14px; border-radius:var(--radius-md); margin-bottom:16px;">
        <div style="font-size:0.85rem; color:#1e40af;">Total Payable Amount</div>
        <div style="font-size:1.6rem; font-weight:800; color:#1d4ed8;">₹${amount}</div>
      </div>

      <div class="form-group">
        <label class="form-label">Select Payment Channel</label>
        <select id="payChannel" class="form-select">
          <option value="UPI (GPay / PhonePe / Paytm)">UPI (Google Pay / PhonePe / Paytm)</option>
          <option value="Net Banking (SBI / Federal Bank / HDFC)">Net Banking (State Bank / Federal / HDFC)</option>
          <option value="Debit Card">Debit / ATM Card</option>
          <option value="Hostel Accounts Cash Counter">Hostel Accounts Counter Challan</option>
        </select>
      </div>

      <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:20px;">
        <button class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
        <button class="btn btn-success" onclick="fees.processPayment('${feeId}')">
          <i class="fa-solid fa-check"></i> Complete Payment
        </button>
      </div>
    `;
    app.showModal(modalHtml);
  },

  async processPayment(feeId) {
    const channel = document.getElementById('payChannel').value;
    const res = await app.api('/fees/pay', {
      method: 'POST',
      body: JSON.stringify({ feeId, paymentMode: channel })
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      fees.render();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  viewReceiptModal(feeId) {
    const f = this.data.find(item => item.id === feeId);
    if (!f) return;

    const modalHtml = `
      <div style="padding:10px;">
        <div style="text-align:center; border-bottom:2px dashed #cbd5e1; padding-bottom:14px; margin-bottom:16px;">
          <div style="font-size:2rem;">🏛️</div>
          <h3 style="font-size:1.3rem; font-weight:800; color:#1e3a8a;">MARIAN HOSTEL ACCOUNTS</h3>
          <p style="font-size:0.75rem; color:var(--text-muted); text-transform:uppercase;">Official Fee Payment Receipt</p>
          <div style="font-family:monospace; font-weight:700; color:var(--primary); margin-top:4px;">${f.receiptNo || 'MH-REC-2026'}</div>
        </div>

        <div style="font-size:0.88rem; line-height:1.7;">
          <div style="display:flex; justify-content:space-between;">
            <span>Student Name:</span> <strong>${f.studentName || app.currentUser.name}</strong>
          </div>
          <div style="display:flex; justify-content:space-between;">
            <span>Room Number:</span> <strong>Room ${f.studentRoom || app.currentUser.roomNo || 'N/A'}</strong>
          </div>
          <div style="display:flex; justify-content:space-between;">
            <span>Billing Month:</span> <strong>${f.month} ${f.year}</strong>
          </div>
          <div style="display:flex; justify-content:space-between;">
            <span>Payment Date:</span> <strong>${f.paymentDate}</strong>
          </div>
          <div style="display:flex; justify-content:space-between;">
            <span>Transaction ID:</span> <strong style="font-family:monospace;">${f.transactionId}</strong>
          </div>
          <div style="display:flex; justify-content:space-between;">
            <span>Mode of Payment:</span> <strong>${f.paymentMode}</strong>
          </div>
        </div>

        <div style="border-top:1px solid #e2e8f0; margin-top:14px; padding-top:10px; font-size:0.85rem; line-height:1.6;">
          <div style="display:flex; justify-content:space-between;"><span>Room Rent:</span> <span>₹${f.rentAmount}</span></div>
          <div style="display:flex; justify-content:space-between;"><span>Mess Charges:</span> <span>₹${f.messAmount}</span></div>
          <div style="display:flex; justify-content:space-between;"><span>Electricity & Water:</span> <span>₹${f.electricityAmount}</span></div>
          <div style="display:flex; justify-content:space-between; font-size:1.1rem; font-weight:800; border-top:1px dashed #cbd5e1; margin-top:6px; padding-top:6px; color:#166534;">
            <span>Total Paid Amount:</span> <span>₹${f.totalAmount}</span>
          </div>
        </div>

        <div style="text-align:center; margin-top:20px; font-size:0.75rem; color:#64748b;">
          Electronically generated voucher • Marian College Hostel Management System
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
          <button class="btn btn-primary" onclick="window.print()"><i class="fa-solid fa-print"></i> Print Receipt</button>
          <button class="btn btn-secondary" onclick="app.closeModal()">Close</button>
        </div>
      </div>
    `;
    app.showModal(modalHtml);
  },

  openWardenEditModal(feeId) {
    const f = this.data.find(item => item.id === feeId);
    if (!f) return;

    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:14px;">
        <i class="fa-solid fa-pen-to-square" style="color:var(--primary);"></i> Edit Fee Record (Warden Control)
      </h3>
      <form onsubmit="fees.saveWardenEdit(event, '${f.id}')">
        <div class="form-group">
          <label class="form-label">Payment Status</label>
          <select id="editFeeStatus" class="form-select">
            <option value="PAID" ${f.status === 'PAID' ? 'selected' : ''}>PAID</option>
            <option value="PENDING" ${f.status === 'PENDING' ? 'selected' : ''}>PENDING</option>
            <option value="OVERDUE" ${f.status === 'OVERDUE' ? 'selected' : ''}>OVERDUE</option>
          </select>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Room Rent (₹)</label>
            <input type="number" id="editFeeRent" class="form-input" value="${f.rentAmount}">
          </div>
          <div class="form-group">
            <label class="form-label">Mess Fee (₹)</label>
            <input type="number" id="editFeeMess" class="form-input" value="${f.messAmount}">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">Electricity / Water / Maintenance (₹)</label>
          <input type="number" id="editFeeElec" class="form-input" value="${f.electricityAmount}">
        </div>

        <div class="form-group">
          <label class="form-label">Warden Remarks / Receipt Notes</label>
          <input type="text" id="editFeeRemarks" class="form-input" value="${f.remarks || ''}" placeholder="e.g. Paid in cash at warden office">
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn btn-primary"><i class="fa-solid fa-floppy-disk"></i> Save Record</button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  async saveWardenEdit(e, feeId) {
    e.preventDefault();
    const payload = {
      feeId,
      status: document.getElementById('editFeeStatus').value,
      rentAmount: parseFloat(document.getElementById('editFeeRent').value) || 0,
      messAmount: parseFloat(document.getElementById('editFeeMess').value) || 0,
      electricityAmount: parseFloat(document.getElementById('editFeeElec').value) || 0,
      remarks: document.getElementById('editFeeRemarks').value.trim()
    };

    const res = await app.api('/fees/update', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      fees.render();
    } else {
      app.showToast(res.message, 'error');
    }
  }
};
