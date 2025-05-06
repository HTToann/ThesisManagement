function openAddMajorModal() {
    const form = document.getElementById('MajorForm');
    form.reset();
    form.majorId.value = "";
    const modal = new bootstrap.Modal(document.getElementById('MajorModal'));
    modal.show();
}

document.getElementById('MajorForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const form = document.getElementById('MajorForm');
    const majorId = form.majorId.value;
    const payload = {
        name: form.name.value,
        faculty_id: form.facultyId.value
    };

    let url = '/SpringThesis/api/secure/majors';
    let method = 'POST';

    if (majorId) {
        url += `/${majorId}`;
        method = 'PUT';
    }

    const response = await fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    });

    if (response.ok) {
        alert(majorId ? "Cập nhật ngành thành công!" : "Thêm ngành thành công!");
        location.reload();
    } else {
        const err = await response.json();
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
});

function editMajorFromButton(button) {
    const major = {
        majorId: button.getAttribute('data-id'),
        name: button.getAttribute('data-name'),
        facultyId: button.getAttribute('data-facultyid')
    };

    const form = document.getElementById('MajorForm');
    form.majorId.value = major.majorId;
    form.name.value = major.name;
    form.facultyId.value = major.facultyId;

    new bootstrap.Modal(document.getElementById('MajorModal')).show();
}

async function deleteMajor(id) {
    if (!confirm("Xác nhận xoá ngành học?"))
        return;

    const response = await fetch(`/SpringThesis/api/secure/majors/${id}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        alert("Đã xoá ngành học!");
        location.reload();
    } else {
        const err = await response.json();
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
}
