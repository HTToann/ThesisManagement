function openAddFacultyModal() {
    const form = document.getElementById('FacultyForm');
    form.reset();
    form.facultyId.value = "";
    const modal = new bootstrap.Modal(document.getElementById('FacultyModal'));
    modal.show();
}

document.getElementById('FacultyForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const form = document.getElementById('FacultyForm');
    const facultyId = form.facultyId.value;
    const payload = { name: form.name.value };

    let url = '/SpringThesis/api/secure/faculty/create';
    let method = 'POST';

    if (facultyId) {
        url = `/SpringThesis/api/secure/faculty/update/${facultyId}`;
        method = 'PUT';
    }

    const response = await fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (response.ok) {
        alert(facultyId ? "Cập nhật khoa thành công!" : "Thêm khoa thành công!");
        location.reload();
    } else {
        const err = await response.json();
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
});

function editFacultyFromButton(button) {
    const faculty = {
        facultyId: button.getAttribute('data-id'),
        name: button.getAttribute('data-name')
    };
    const form = document.getElementById('FacultyForm');
    form.facultyId.value = faculty.facultyId;
    form.name.value = faculty.name;
    new bootstrap.Modal(document.getElementById('FacultyModal')).show();
}

function deleteFaculty(id) {
    if (!confirm("Xác nhận xoá khoa?"))
        return;

    fetch(`/SpringThesis/api/secure/faculty/delete/${id}`, {
        method: 'DELETE'
    }).then(res => {
        if (res.ok) {
            alert("Đã xoá khoa!");
            location.reload();
        } else {
            alert("Lỗi khi xoá khoa!");
        }
    });
}
