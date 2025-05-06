function openAddGradeModal() {
    const form = document.getElementById("GradeForm");
    form.reset();
    form.mode.value = "add";
    new bootstrap.Modal(document.getElementById("GradeModal")).show();
}

function editGrade(boardId, thesisId, lecturerId, criteriaId, score) {
    const form = document.getElementById("GradeForm");
    form.mode.value = "edit";
    form.boardId.value = boardId;
    form.thesisId.value = thesisId;
    form.lecturerId.value = lecturerId;
    form.criteriaId.value = criteriaId;
    form.score.value = score;

    new bootstrap.Modal(document.getElementById("GradeModal")).show();
}

document.getElementById("GradeForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const form = e.target;
    const payload = {
        boardId: form.boardId.value,
        thesisId: form.thesisId.value,
        lecturerId: form.lecturerId.value,
        criteriaId: form.criteriaId.value,
        score: form.score.value
    };

    const url = "/SpringThesis/api/secure/thesis-grades";
    const method = form.mode.value === "edit" ? "PUT" : "POST";

    const res = await fetch(url, {
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

function deleteGrade(boardId, thesisId, lecturerId, criteriaId) {
    if (!confirm("Bạn có chắc chắn muốn xoá điểm này?")) return;

    const url = `/SpringThesis/api/secure/thesis-grades/${boardId}/${thesisId}/${lecturerId}/${criteriaId}`;
    fetch(url, { method: "DELETE" })
        .then(res => {
            if (res.ok) {
                alert("Xoá thành công!");
                location.reload();
            } else {
                alert("Không thể xoá điểm.");
            }
        })
        .catch(() => alert("Lỗi kết nối khi xoá."));
}
