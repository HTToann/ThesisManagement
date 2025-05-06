function openAddTLModal() {
    const form = document.getElementById("TLForm");
    form.reset();
    form.mode.value = "add";
    new bootstrap.Modal(document.getElementById("TLModal")).show();
}

function editTL(thesisId, lecturerId, lectureRole) {
    const form = document.getElementById("TLForm");
    form.mode.value = "edit";

    form.thesisId.value = thesisId;
    form.oldLecturerId.value = lecturerId; // 👈 hidden input
    form.lecturerId.value = lecturerId;
    form.lectureRole.value = lectureRole;

    new bootstrap.Modal(document.getElementById("TLModal")).show();
}

document.getElementById("TLForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const form = e.target;
    let payload = {};

    if (form.mode.value === "edit") {
        // Khi sửa: gửi các field khớp với BE
        payload = {
            thesisId: form.thesisId.value,
            oldLecturerId: form.oldLecturerId.value,
            newLecturerId: form.lecturerId.value,
            lectureRole: form.lectureRole.value
        };
    } else {
        // Khi thêm mới
        payload = {
            thesisId: form.thesisId.value,
            lecturerId: form.lecturerId.value,
            lectureRole: form.lectureRole.value
        };
    }

    const method = form.mode.value === "edit" ? "PATCH" : "POST";
    const res = await fetch("/SpringThesis/api/secure/thesis-lecturers", {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        alert("Lưu thành công!");
        location.reload();
    } else {
        const err = await res.json().catch(() => ({}));
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
});

function deleteTL(thesisId, lecturerId) {
    if (!confirm("Bạn có chắc muốn xoá phân công này?")) return;

    fetch("/SpringThesis/api/secure/thesis-lecturers", {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            thesisId: thesisId,
            lecturerId: lecturerId
        })
    }).then(res => {
        if (res.ok) {
            alert("Đã xoá!");
            location.reload();
        } else {
            res.json().then(err => {
                alert("Lỗi: " + (err.error || "Không thể xoá phân công."));
            }).catch(() => {
                alert("Không thể xoá phân công.");
            });
        }
    }).catch(() => alert("Lỗi kết nối!"));
}
