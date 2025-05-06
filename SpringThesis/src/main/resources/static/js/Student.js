function openAddStudentModal() {
    const form = document.getElementById("StudentForm");
    form.reset();
    form.setAttribute("data-mode", "add");
    form.userId.disabled = false;
    new bootstrap.Modal(document.getElementById("StudentModal")).show();
}

function editStudent(id, username, thesisId, majorId) {
    const form = document.getElementById("StudentForm");
    form.setAttribute("data-mode", "edit");

    form.studentId.value = id;
    form.userId.value = username;
    form.userId.disabled = true;
    form.thesisId.value = thesisId || "";

    // Gán majorId duy nhất
    form.majorIds.value = majorId;

    new bootstrap.Modal(document.getElementById("StudentModal")).show();
}

document.getElementById("StudentForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const form = e.target;
    const mode = form.getAttribute("data-mode");
    const payload = {
        userId: form.userId.value,
        thesisId: form.thesisId.value || null,
        majorId: form.majorIds.value // Gửi dưới dạng chuỗi
    };

    let url = "/SpringThesis/api/secure/student";
    let method = "POST";

    if (mode === "edit") {
        url += "/" + form.studentId.value;
        method = "PATCH";
    }

    const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    if (response.ok) {
        alert("Lưu thành công!");
        location.reload();
    } else {
        const err = await response.json().catch(() => ({}));
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
});

function deleteStudent(id) {
    if (!confirm("Bạn có chắc muốn xoá sinh viên này?")) return;

    fetch(`/SpringThesis/api/secure/student/${id}`, {
        method: "DELETE"
    }).then(res => {
        if (res.ok) {
            alert("Đã xoá!");
            location.reload();
        } else {
            alert("Không thể xoá sinh viên!");
        }
    }).catch(() => alert("Lỗi kết nối!"));
}
