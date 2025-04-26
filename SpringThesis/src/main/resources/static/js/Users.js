function openAddUserModal() {
    const form = document.getElementById('UserForm');
    form.reset(); // Clear hết form
    form.userId.value = ""; // Xóa userId để phân biệt thêm mới
    document.getElementById('avatarInput').required = true; // Bắt required avatar khi thêm mới
    const modal = new bootstrap.Modal(document.getElementById('UserModal'));
    modal.show();
 
}

document.getElementById('UserForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const form = document.getElementById('UserForm');
    const formData = new FormData(form);
    const userId = form.userId.value; // Lấy hidden userId

    let url, method;
    if (userId) {
        // Nếu có userId => gọi API cập nhật
        url = `/SpringThesis/api/users/${userId}`;
        method = 'PUT';
    } else {
        // Nếu không có => gọi API thêm mới
        url = '/SpringThesis/api/users/register';
        method = 'POST';
    }

    const response = await fetch(url, {
        method: method,
        body: formData // Không cần set headers
    });

    if (response.ok) {
        alert(userId ? "Cập nhật người dùng thành công!" : "Thêm người dùng thành công!");
        location.reload();
    } else {
        const err = await response.json();
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
});

function editUserFromButton(button) {
    const user = {
        userId: button.getAttribute('data-userid'),
        username: button.getAttribute('data-username'),
        password: button.getAttribute('data-password'),
        firstName: button.getAttribute('data-firstname'),
        lastName: button.getAttribute('data-lastname'),
        email: button.getAttribute('data-email'),
        phone: button.getAttribute('data-phone'),
        role: button.getAttribute('data-role'),
        facultyId: {facultyId: parseInt(button.getAttribute('data-facultyid'))},
        avatar: button.getAttribute('data-avatar') // 🛑 thêm dòng này nè!
    };

    editUser(user);
}
function editUser(user) {
    const form = document.getElementById('UserForm');
    form.userId.value = user.userId;
    form.username.value = user.username;
    form.password.value = user.password;
    form.firstName.value = user.firstName;
    form.lastName.value = user.lastName;
    form.email.value = user.email;
    form.phone.value = user.phone;
    form.role.value = user.role;
    form.facultyId.value = user.facultyId.facultyId;
    // Xử lý avatar
    const avatarInput = document.getElementById('avatarInput');
    if (user.avatar) {
        avatarInput.required = false;
    } else {
        avatarInput.required = true;
    }
    new bootstrap.Modal(document.getElementById('UserModal')).show();
}
function deleteUser(id) {
    if (!confirm("Xác nhận xoá user?"))
        return;

    fetch(`/SpringThesis/api/secure/users/delete/${id}`, {
        method: 'DELETE'
    }).then(res => {
        if (res.ok) {
            alert("Đã xoá user!");
            location.reload();
        } else {
            alert("Lỗi khi xoá user!");
        }
    });
}

