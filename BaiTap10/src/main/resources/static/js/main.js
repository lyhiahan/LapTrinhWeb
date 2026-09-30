let fullToken = '';
let isTokenMasked = true;

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function maskToken(token) {
    if (!token) return '';
    const parts = token.split('.');
    if (parts.length === 3) {
        // Che phần payload nhạy cảm ở giữa
        return `${parts[0]}.••••••••••••[PAYLOAD_HIDDEN]••••••••••••.${parts[2].slice(-10)}`;
    }
    return token.slice(0, 10) + '••••••••' + token.slice(-6);
}

function toggleTokenMask() {
    const tokenDisplay = document.getElementById('tokenDisplay');
    const btnToggle = document.getElementById('btnToggleToken');
    if (!tokenDisplay || !fullToken) return;

    isTokenMasked = !isTokenMasked;
    if (isTokenMasked) {
        tokenDisplay.textContent = maskToken(fullToken);
        btnToggle.textContent = '👁️ Hiện đầy đủ';
    } else {
        tokenDisplay.textContent = fullToken;
        btnToggle.textContent = '🔒 Che bảo mật';
    }
}

async function copyTokenToClipboard() {
    if (!fullToken) return;
    try {
        await navigator.clipboard.writeText(fullToken);
        alert('Đã sao chép toàn bộ Token vào bộ nhớ tạm (Clipboard)!');
    } catch (err) {
        // Fallback
        const textarea = document.createElement('textarea');
        textarea.value = fullToken;
        document.body.appendChild(textarea);
        textarea.select();
        document.execCommand('copy');
        document.body.removeChild(textarea);
        alert('Đã sao chép toàn bộ Token vào Clipboard!');
    }
}

function switchTab(tab) {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');
    const tabLoginBtn = document.getElementById('tabLoginBtn');
    const tabSignupBtn = document.getElementById('tabSignupBtn');
    const alertBox = document.getElementById('alertBox');

    if (alertBox) alertBox.style.display = 'none';

    if (tab === 'login') {
        loginForm.style.display = 'block';
        signupForm.style.display = 'none';
        tabLoginBtn.classList.add('active');
        tabSignupBtn.classList.remove('active');
    } else {
        loginForm.style.display = 'none';
        signupForm.style.display = 'block';
        tabLoginBtn.classList.remove('active');
        tabSignupBtn.classList.add('active');
    }
}

function showAlert(message, isError = false) {
    const alertBox = document.getElementById('alertBox');
    if (!alertBox) return;
    alertBox.textContent = message;
    alertBox.className = isError ? 'alert alert-error' : 'alert alert-success';
    alertBox.style.display = 'block';
}

async function handleLogin(event) {
    event.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    const submitBtn = event.target.querySelector('button[type="submit"]');

    submitBtn.disabled = true;
    submitBtn.textContent = 'Đang xử lý đăng nhập...';

    try {
        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.ok && data.token) {
            localStorage.setItem('jwtToken', data.token);
            showAlert('Đăng nhập thành công! Đang chuyển hướng sang Profile...');
            setTimeout(() => {
                window.location.href = '/profile';
            }, 700);
        } else {
            const msg = data.detail || data.description || 'Đăng nhập thất bại!';
            showAlert(`[Lỗi ${response.status}] ${msg}`, true);
        }
    } catch (err) {
        showAlert('Lỗi kết nối tới máy chủ!', true);
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Đăng nhập';
    }
}

async function handleSignup(event) {
    event.preventDefault();
    const fullName = document.getElementById('signupFullName').value;
    const email = document.getElementById('signupEmail').value;
    const password = document.getElementById('signupPassword').value;
    const submitBtn = event.target.querySelector('button[type="submit"]');

    submitBtn.disabled = true;
    submitBtn.textContent = 'Đang tạo tài khoản...';

    try {
        const response = await fetch('/auth/signup', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ fullName, email, password })
        });

        if (response.ok) {
            showAlert('Đăng ký tài khoản thành công! Hãy chuyển sang Đăng nhập.');
            setTimeout(() => switchTab('login'), 1200);
        } else {
            const errData = await response.json();
            showAlert(errData.detail || errData.description || 'Đăng ký thất bại!', true);
        }
    } catch (err) {
        showAlert('Lỗi kết nối tới máy chủ!', true);
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Tạo tài khoản';
    }
}

async function loadProfileData() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        alert('Vui lòng đăng nhập trước!');
        window.location.href = '/login';
        return;
    }

    fullToken = token;
    const tokenDisplay = document.getElementById('tokenDisplay');
    if (tokenDisplay) {
        tokenDisplay.textContent = isTokenMasked ? maskToken(fullToken) : fullToken;
    }

    let currentUserId = null;
    let currentUserRole = "ROLE_USER";

    // 1. Lấy thông tin /users/me
    try {
        const resMe = await fetch('/users/me', {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (resMe.ok) {
            const user = await resMe.json();
            currentUserId = user.id;
            currentUserRole = user.role || "ROLE_USER";

            document.getElementById('userId').textContent = user.id;
            document.getElementById('userFullName').textContent = user.fullName;
            document.getElementById('userEmail').textContent = user.email;

            const roleEl = document.getElementById('userRole');
            if (roleEl) roleEl.textContent = user.role || "ROLE_USER";

            const isNonLocked = user.accountNonLocked !== false;
            document.getElementById('userLockStatus').innerHTML = isNonLocked
                ? '<span class="badge badge-active">Hoạt động bình thường</span>'
                : '<span class="badge badge-locked">Đã bị khóa</span>';
        } else {
            const errData = await resMe.json();
            alert(`Lỗi xác thực: [${resMe.status}] ${errData.detail || errData.description}`);
            localStorage.removeItem('jwtToken');
            window.location.href = '/login';
            return;
        }
    } catch (e) {
        console.error('Error fetching /users/me:', e);
    }

    // 2. Lấy danh sách /users (chống XSS bằng textContent)
    try {
        const resUsers = await fetch('/users', {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (resUsers.ok) {
            const users = await resUsers.json();
            const tbody = document.getElementById('userListBody');
            tbody.innerHTML = '';

            users.forEach(u => {
                const tr = document.createElement('tr');

                const tdId = document.createElement('td');
                tdId.textContent = u.id;
                tr.appendChild(tdId);

                const tdName = document.createElement('td');
                tdName.textContent = u.fullName;
                tr.appendChild(tdName);

                const tdEmail = document.createElement('td');
                tdEmail.textContent = u.email;
                tr.appendChild(tdEmail);

                const tdRole = document.createElement('td');
                tdRole.textContent = u.role || 'ROLE_USER';
                tr.appendChild(tdRole);

                const tdStatus = document.createElement('td');
                const isNonLocked = u.accountNonLocked !== false;
                const badge = document.createElement('span');
                badge.className = `badge ${isNonLocked ? 'badge-active' : 'badge-locked'}`;
                badge.textContent = isNonLocked ? 'Hoạt động' : 'Bị khóa';
                tdStatus.appendChild(badge);
                tr.appendChild(tdStatus);

                const tdAction = document.createElement('td');
                if (currentUserRole !== 'ROLE_ADMIN') {
                    const noPerm = document.createElement('span');
                    noPerm.style.color = '#94a3b8';
                    noPerm.style.fontSize = '0.85rem';
                    noPerm.style.fontStyle = 'italic';
                    noPerm.textContent = 'Chỉ Quản trị viên';
                    tdAction.appendChild(noPerm);
                } else {
                    const btn = document.createElement('button');
                    btn.className = `btn-action ${isNonLocked ? 'btn-lock' : 'btn-unlock'}`;
                    btn.textContent = isNonLocked ? 'Khóa tài khoản' : 'Mở khóa';

                    if (u.id === currentUserId) {
                        btn.disabled = true;
                        btn.title = "Không thể tự khóa chính mình";
                        btn.style.opacity = "0.4";
                        btn.style.cursor = "not-allowed";
                    } else {
                        btn.onclick = () => toggleLock(u.id, isNonLocked, btn);
                    }

                    tdAction.appendChild(btn);
                }
                tr.appendChild(tdAction);

                tbody.appendChild(tr);
            });
        }
    } catch (e) {
        console.error('Error fetching /users:', e);
    }
}

async function toggleLock(userId, isCurrentlyNonLocked, btn) {
    // Bước xác nhận đối với hành động nguy hiểm Khóa tài khoản
    if (isCurrentlyNonLocked) {
        const confirmLock = confirm(
            `CẢNH BÁO NGUY HIỂM:\nBạn có chắc chắn muốn KHÓA tài khoản có ID = ${userId} không?\nTài khoản này sẽ bị thu hồi toàn bộ quyền truy cập ngay lập tức!`
        );
        if (!confirmLock) return;
    }

    const token = localStorage.getItem('jwtToken');
    const endpoint = isCurrentlyNonLocked ? `/users/${userId}/lock` : `/users/${userId}/unlock`;

    if (btn) {
        btn.disabled = true;
        btn.textContent = 'Đang xử lý...';
    }

    try {
        const res = await fetch(endpoint, {
            method: 'PATCH',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (res.ok) {
            loadProfileData();
        } else {
            const err = await res.json();
            alert(`[Lỗi ${res.status}] ${err.detail || err.description}`);
            if (btn) {
                btn.disabled = false;
                btn.textContent = isCurrentlyNonLocked ? 'Khóa tài khoản' : 'Mở khóa';
            }
        }
    } catch (e) {
        alert('Lỗi kết nối khi cập nhật trạng thái tài khoản!');
        if (btn) {
            btn.disabled = false;
            btn.textContent = isCurrentlyNonLocked ? 'Khóa tài khoản' : 'Mở khóa';
        }
    }
}

function showTestResult(status, body) {
    const box = document.getElementById('testResultBox');
    box.style.display = 'block';
    box.textContent = `HTTP Status Code: ${status}\nPhản hồi ProblemDetail JSON:\n${JSON.stringify(body, null, 2)}`;
}

// 1. Kiểm thử JWT sai cú pháp -> HTTP 401
async function testInvalidFormatToken(btn) {
    if (btn) btn.disabled = true;
    try {
        const res = await fetch('/users/me', {
            headers: { 'Authorization': 'Bearer token-sai-dinh-dang' }
        });
        const data = await res.json();
        showTestResult(res.status, data);
    } catch (err) {
        alert('Lỗi kết nối kiểm thử!');
    } finally {
        if (btn) btn.disabled = false;
    }
}

// 2. Kiểm thử JWT chữ ký giả mạo -> HTTP 401
async function testInvalidSignatureToken(btn) {
    if (btn) btn.disabled = true;
    try {
        const fakeToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImlhdCI6MTYwMDAwMDAwMCwiZXhwIjoyMDAwMDAwMDAwfQ.fake_signature_not_matching_secret_key_123456";
        const res = await fetch('/users/me', {
            headers: { 'Authorization': `Bearer ${fakeToken}` }
        });
        const data = await res.json();
        showTestResult(res.status, data);
    } catch (err) {
        alert('Lỗi kết nối kiểm thử!');
    } finally {
        if (btn) btn.disabled = false;
    }
}

// 3. Kiểm thử JWT đã hết hạn THỰC SỰ -> HTTP 401 (JWT đã hết hạn)
async function testExpiredToken(btn) {
    if (btn) btn.disabled = true;
    try {
        // Lấy token hết hạn thật (ký bằng chính secretKey của server)
        const tokenRes = await fetch('/auth/sample-expired-token');
        const tokenData = await tokenRes.json();
        const expiredToken = tokenData.expiredToken;

        const res = await fetch('/users/me', {
            headers: { 'Authorization': `Bearer ${expiredToken}` }
        });
        const data = await res.json();
        showTestResult(res.status, data);
    } catch (err) {
        alert('Lỗi khi kiểm thử token hết hạn!');
    } finally {
        if (btn) btn.disabled = false;
    }
}

// 4. Kiểm thử không gửi Token -> HTTP 401 (Chưa xác thực)
async function testNoToken(btn) {
    if (btn) btn.disabled = true;
    try {
        const res = await fetch('/users/me');
        const data = await res.json();
        showTestResult(res.status, data);
    } catch (err) {
        alert('Lỗi kết nối kiểm thử!');
    } finally {
        if (btn) btn.disabled = false;
    }
}

function handleLogout() {
    localStorage.removeItem('jwtToken');
    window.location.href = '/login';
}
