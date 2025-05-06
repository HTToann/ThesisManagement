async function createBoard() {
    const confirmed = confirm("Xác nhận tạo hội đồng mới?");
    if (!confirmed) return;

    const response = await fetch('/SpringThesis/api/secure/boards', {
        method: 'POST'
    });

    if (response.ok) {
        alert("Tạo hội đồng thành công!");
        location.reload();
    } else {
        const err = await response.json().catch(() => ({}));
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
}
async function toggleLockBoard(boardId, isLocked) {
    const action = isLocked ? "mở khoá" : "khoá";
    if (!confirm(`Bạn có chắc chắn muốn ${action} hội đồng này?`)) return;

    const url = `/SpringThesis/api/secure/boards/${boardId}`;
    const payload = {
        isLocked: (!isLocked).toString()
    };

    const response = await fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    });

    if (response.ok) {
        alert(`Đã ${action} hội đồng.`);
        location.reload();
    } else {
        const err = await response.json().catch(() => ({}));
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
}


//async function deleteBoard(id) {
//    if (!confirm("Bạn có chắc chắn muốn xoá hội đồng này?")) return;
//
//    const response = await fetch(`/SpringThesis/api/secure/boards/${id}`, {
//        method: 'DELETE'
//    });
//
//    if (response.ok) {
//        alert("Đã xoá hội đồng!");
//        location.reload();
//    } else {
//        const err = await response.json().catch(() => ({}));
//        alert("Lỗi: " + (err.error || "Không xác định"));
//    }
//}
