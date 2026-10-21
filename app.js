/**
 * ShopSphere Application JavaScript
 */
document.addEventListener("DOMContentLoaded", () => {
    // Auto-dismiss alert toasts after 4 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }, 4000);
    });

    // Form validation tooltips & check
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
});

/**
 * Helper to update cart quantity with form submission
 */
function updateCartQty(btn, delta) {
    const form = btn.closest('form');
    if (form) {
        const input = form.querySelector('input[name="quantity"]');
        if (input) {
            let val = parseInt(input.value) || 1;
            val += delta;
            if (val >= 1) {
                input.value = val;
                form.submit();
            }
        }
    }
}
