// ========================================
// E-Commerce Platform - Main JavaScript
// ========================================

// Cart state - will be synced with session
let cart = [];

// ========================================
// Cart Management - Session-based API
// ========================================

// Load cart from server session
async function loadCartFromSession() {
    try {
        const response = await fetch('/api/cart');
        const data = await response.json();
        if (data.success) {
            cart = data.items || [];
            updateCartUI();
        } else {
            console.error('Erreur lors du chargement du panier:', data.error);
        }
    } catch (error) {
        console.error('Erreur lors du chargement du panier:', error);
    }
}

function addToCart(productId, quantity = 1) {
    // Validate inputs
    if (!productId) {
        showNotification('ID produit invalide', 'error');
        return;
    }

    if (quantity < 1 || quantity > 100) {
        showNotification('Quantité invalide (1-100)', 'error');
        return;
    }

    fetch(`/api/cart/add?produitId=${productId}&quantite=${quantity}`, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            loadCartFromSession();
            showNotification(data.message || 'Produit ajouté au panier!');
        } else {
            showNotification(data.error || 'Erreur lors de l\'ajout au panier', 'error');
        }
    })
    .catch(error => {
        console.error('Erreur:', error);
        showNotification(error.message || 'Erreur lors de l\'ajout au panier', 'error');
    });
}

function removeFromCart(productId) {
    if (!productId) {
        showNotification('ID produit invalide', 'error');
        return;
    }

    fetch(`/api/cart/remove/${productId}`, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            loadCartFromSession();
            showNotification(data.message || 'Produit retiré du panier');
        } else {
            showNotification(data.error || 'Erreur lors de la suppression', 'error');
        }
    })
    .catch(error => {
        console.error('Erreur:', error);
        showNotification('Erreur lors de la suppression', 'error');
    });
}

function updateQuantity(productId, quantity) {
    if (!productId) {
        showNotification('ID produit invalide', 'error');
        return;
    }

    if (quantity < 0 || quantity > 100) {
        showNotification('Quantité invalide', 'error');
        return;
    }

    fetch(`/api/cart/update/${productId}?quantite=${quantity}`, {
        method: 'PUT'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            loadCartFromSession();
        } else {
            showNotification(data.error || 'Erreur lors de la mise à jour', 'error');
        }
    })
    .catch(error => {
        console.error('Erreur:', error);
        showNotification('Erreur lors de la mise à jour', 'error');
    });
}

function clearCart() {
    fetch('/api/cart/clear', {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            loadCartFromSession();
            showNotification(data.message || 'Panier vidé');
        } else {
            showNotification(data.error || 'Erreur lors du vidage du panier', 'error');
        }
    })
    .catch(error => {
        console.error('Erreur:', error);
        showNotification('Erreur lors du vidage du panier', 'error');
    });
}

// Legacy function for local storage fallback
function saveCart() {
    localStorage.setItem('cart', JSON.stringify(cart));
}

async function getCartFromSession() {
    try {
        const response = await fetch('/api/cart');
        const data = await response.json();
        if (data.success) {
            return data;
        }
        return null;
    } catch (error) {
        console.error('Erreur:', error);
    }
    return null;
}

// Validate cart before checkout
async function validateCart() {
    try {
        const response = await fetch('/api/cart/validate');
        const data = await response.json();
        return data.success && data.isValid;
    } catch (error) {
        console.error('Erreur:', error);
        return false;
    }
}

function getCartTotal() {
    return cart.reduce((total, item) => total + (item.prix * item.quantite), 0);
}

function getCartCount() {
    return cart.reduce((count, item) => count + item.quantite, 0);
}

function updateCartUI() {
    // Update cart badge
    const cartBadge = document.querySelector('.cart-badge');
    if (cartBadge) {
        cartBadge.textContent = getCartCount();
    }

    // Update cart page if visible
    const cartItemsContainer = document.getElementById('cart-items');
    if (cartItemsContainer) {
        renderCartItems();
    }

    // Update cart summary if visible
    const cartTotal = document.getElementById('cart-total');
    const cartSubtotal = document.getElementById('cart-subtotal');
    if (cartTotal) {
        cartTotal.textContent = formatPrice(getCartTotal());
    }
    if (cartSubtotal) {
        cartSubtotal.textContent = formatPrice(getCartTotal());
    }
}

function renderCartItems() {
    const container = document.getElementById('cart-items');
    if (!container) return;

    if (cart.length === 0) {
        container.innerHTML = `
            <div class="empty-cart">
                <i class="fas fa-shopping-cart"></i>
                <h3>Votre panier est vide</h3>
                <p>Découvrez nos produits et ajoutez-les à votre panier</p>
                <a href="/produits" class="btn btn-primary">Voir les produits</a>
            </div>
        `;
        return;
    }

    container.innerHTML = cart.map(item => `
        <div class="cart-item" data-id="${item.produitId}">
            <div class="cart-product">
                <div class="cart-product-image">
                    <i class="fas fa-box" style="font-size: 2rem; color: #ccc;"></i>
                </div>
                <div class="cart-product-info">
                    <h4>${item.nom}</h4>
                    <p>Prix unitaire: ${formatPrice(item.prix)}</p>
                </div>
            </div>
            <div class="cart-price">${formatPrice(item.prix)}</div>
            <div class="cart-quantity">
                <div class="quantity-controls">
                    <button class="quantity-btn" onclick="updateQuantity(${item.produitId}, ${item.quantite - 1})">-</button>
                    <span class="quantity-value">${item.quantite}</span>
                    <button class="quantity-btn" onclick="updateQuantity(${item.produitId}, ${item.quantite + 1})">+</button>
                </div>
            </div>
            <div class="cart-total">${formatPrice(item.prix * item.quantite)}</div>
            <button class="remove-btn" onclick="removeFromCart(${item.produitId})">
                <i class="fas fa-trash"></i>
            </button>
        </div>
    `).join('');
}

// ========================================
// Product Display
// ========================================

async function loadProducts(categoryId = null) {
    try {
        const url = categoryId
            ? `/api/produits?categorieId=${categoryId}`
            : '/api/produits';

        const response = await fetch(url);
        const products = await response.json();

        renderProducts(products);
    } catch (error) {
        console.error('Erreur lors du chargement des produits:', error);
    }
}

function renderProducts(products) {
    const container = document.getElementById('products-grid');
    if (!container) return;

    if (products.length === 0) {
        container.innerHTML = '<p class="no-products">Aucun produit trouvé</p>';
        return;
    }

    container.innerHTML = products.map(product => `
        <div class="product-card">
            <div class="product-image">
                <i class="fas fa-box" style="font-size: 5rem; color: #ccc;"></i>
                <div class="product-actions">
                    <button class="product-action-btn" onclick="viewProduct(${product.id})" title="Voir">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="product-action-btn" onclick="addToCart(${product.id})" title="Ajouter au panier">
                        <i class="fas fa-cart-plus"></i>
                    </button>
                </div>
            </div>
            <div class="product-info">
                <span class="product-category">${product.categorie?.nom || 'Catégorie'}</span>
                <h3 class="product-title">${product.nom}</h3>
                <div class="product-price">
                    <span class="current-price">${formatPrice(product.prix)}</span>
                    ${product.ancienPrix ? `<span class="original-price">${formatPrice(product.ancienPrix)}</span>` : ''}
                </div>
                <div class="product-rating">
                    <span class="stars">${getStars(product.note || 4)}</span>
                    <span class="rating-count">(${product.nombreAvis || 0} avis)</span>
                </div>
                <button class="add-to-cart-btn" onclick="addToCart(${product.id})">Ajouter au panier</button>
            </div>
        </div>
    `).join('');
}

async function loadProductDetail(productId) {
    try {
        const response = await fetch(`/api/produit/${productId}`);
        const product = await response.json();

        renderProductDetail(product);
    } catch (error) {
        console.error('Erreur lors du chargement du produit:', error);
        showNotification('Erreur lors du chargement du produit', 'error');
    }
}

function renderProductDetail(product) {
    // Update page elements with product data
    const elements = {
        'product-name': product.nom,
        'product-price': formatPrice(product.prix),
        'product-description': product.description || 'Aucune description disponible.',
        'product-category': product.categorie?.nom || 'Catégorie',
        'product-stock': product.stock > 0 ? `En stock (${product.stock} unités)` : 'Rupture de stock'
    };

    Object.entries(elements).forEach(([id, value]) => {
        const element = document.getElementById(id);
        if (element) element.textContent = value;
    });

    // Update add to cart button
    const addToCartBtn = document.getElementById('add-to-cart-btn');
    if (addToCartBtn) {
        addToCartBtn.onclick = () => {
            const quantity = parseInt(document.getElementById('quantity')?.value || 1);
            addToCart(product.id, quantity);
        };
    }
}

// ========================================
// Categories
// ========================================

async function loadCategories() {
    try {
        const response = await fetch('/api/categories');
        const categories = await response.json();

        renderCategories(categories);
    } catch (error) {
        console.error('Erreur lors du chargement des catégories:', error);
    }
}

function renderCategories(categories) {
    // Update category filter buttons
    const container = document.getElementById('categories-filter');
    if (container) {
        container.innerHTML = `
            <button class="filter-btn active" onclick="filterProducts(null)">Tous</button>
            ${categories.map(cat => `
                <button class="filter-btn" onclick="filterProducts(${cat.id})">${cat.nom}</button>
            `).join('')}
        `;
    }

    // Update category cards on home page
    const categoriesContainer = document.getElementById('categories-grid');
    if (categoriesContainer) {
        const icons = ['fas fa-laptop', 'fas fa-mobile-alt', 'fas fa-headphones', 'fas fa-camera', 'fas fa-gamepad', 'fas fa-clock'];
        categoriesContainer.innerHTML = categories.map((cat, index) => `
            <div class="category-card" onclick="filterProducts(${cat.id})">
                <i class="${icons[index % icons.length]}"></i>
                <h3>${cat.nom}</h3>
                <p>${cat.description || 'Voir les produits'}</p>
            </div>
        `).join('');
    }
}

function filterProducts(categoryId) {
    // Update active button
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');

    // Load products with filter
    loadProducts(categoryId);
}

// ========================================
// Order Management
// ========================================

async function placeOrder(clientData) {
    try {
        // First validate cart
        const isValid = await validateCart();
        if (!isValid) {
            showNotification('Votre panier est vide ou invalide', 'error');
            return;
        }

        // Load cart from session to ensure we have latest data
        const cartData = await getCartFromSession();
        const currentCart = cartData?.items || [];

        if (currentCart.length === 0) {
            showNotification('Votre panier est vide', 'error');
            return;
        }

        const orderData = {
            client: clientData,
            elements: currentCart.map(item => ({
                produitId: item.produitId,
                quantite: item.quantite,
                prixUnitaire: item.prix
            })),
            montantTotal: cartData.total
        };

        const response = await fetch('/api/commande', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        });

        if (response.ok) {
            const result = await response.json();
            if (result.success) {
                const order = result.order || {};
                // Store total for confirmation page
                localStorage.setItem('lastOrderTotal', formatPrice(cartData.total));
                clearCart();
                window.location.href = `/confirmation-commande?id=${order.id}`;
            } else {
                showNotification(result.message || 'Erreur lors de la commande', 'error');
            }
        } else {
            const error = await response.json();
            showNotification(error.message || 'Erreur lors de la commande', 'error');
        }
    } catch (error) {
        console.error('Erreur lors de la commande:', error);
        showNotification('Erreur lors de la commande', 'error');
    }
}

function validateOrderForm() {
    const requiredFields = ['nom', 'email', 'telephone', 'adresse', 'ville', 'codePostal'];
    let isValid = true;

    requiredFields.forEach(field => {
        const input = document.getElementById(field);
        if (input && !input.value.trim()) {
            input.style.borderColor = 'red';
            isValid = false;
        } else if (input) {
            input.style.borderColor = '';
        }
    });

    return isValid;
}

function handleOrderSubmit(event) {
    event.preventDefault();

    // Validate cart first
    validateCart().then(isValid => {
        if (!isValid) {
            showNotification('Votre panier est vide ou invalide', 'error');
            return;
        }

        getCartFromSession().then(cartData => {
            const currentCart = cartData?.items || [];

            if (currentCart.length === 0) {
                showNotification('Votre panier est vide', 'error');
                return;
            }

            if (!validateOrderForm()) {
                showNotification('Veuillez remplir tous les champs obligatoires', 'error');
                return;
            }

            const clientData = {
                nom: document.getElementById('nom').value,
                email: document.getElementById('email').value,
                telephone: document.getElementById('telephone').value,
                adresse: document.getElementById('adresse').value,
                ville: document.getElementById('ville').value,
                codePostal: document.getElementById('codePostal').value
            };

            placeOrder(clientData);
        });
    });
}

// ========================================
// Utility Functions
// ========================================

function formatPrice(price) {
    return new Intl.NumberFormat('fr-FR', {
        style: 'currency',
        currency: 'EUR'
    }).format(price);
}

function getStars(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    let stars = '';

    for (let i = 0; i < 5; i++) {
        if (i < fullStars) {
            stars += '<i class="fas fa-star"></i>';
        } else if (i === fullStars && hasHalfStar) {
            stars += '<i class="fas fa-star-half-alt"></i>';
        } else {
            stars += '<i class="far fa-star"></i>';
        }
    }

    return stars;
}

function getProductById(productId) {
    // This would normally fetch from API, but for simplicity we store product data
    const products = window.productsCache || [];
    return products.find(p => p.id === productId);
}

function viewProduct(productId) {
    window.location.href = `/produit/${productId}`;
}

function showNotification(message, type = 'success') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
        <span>${message}</span>
    `;

    // Add styles
    notification.style.cssText = `
        position: fixed;
        top: 100px;
        right: 20px;
        background: ${type === 'success' ? '#27ae60' : '#e74c3c'};
        color: white;
        padding: 15px 25px;
        border-radius: 5px;
        box-shadow: 0 5px 20px rgba(0,0,0,0.2);
        z-index: 10000;
        animation: slideIn 0.3s ease;
        display: flex;
        align-items: center;
        gap: 10px;
    `;

    document.body.appendChild(notification);

    // Remove after 3 seconds
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// ========================================
// Initialization
// ========================================

document.addEventListener('DOMContentLoaded', function() {
    // Load cart from session on page load
    loadCartFromSession();

    // Load categories
    loadCategories();

    // Load products if on products page
    const productsGrid = document.getElementById('products-grid');
    if (productsGrid) {
        loadProducts();
    }

    // Load product detail if on detail page
    const productDetail = document.getElementById('product-detail');
    if (productDetail) {
        const productId = new URLSearchParams(window.location.search).get('id');
        if (productId) {
            loadProductDetail(parseInt(productId));
        }
    }

    // Setup order form
    const orderForm = document.getElementById('order-form');
    if (orderForm) {
        orderForm.addEventListener('submit', handleOrderSubmit);
    }

    // Quantity controls
    document.querySelectorAll('.quantity-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const input = this.parentElement.querySelector('.quantity-value, input');
            if (input) {
                let value = parseInt(input.value) || 1;
                if (this.textContent === '+') {
                    value++;
                } else if (this.textContent === '-') {
                    value--;
                }
                value = Math.max(1, value);
                input.value = value;
            }
        });
    });
});

// Add animation keyframes
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// ========================================
// Payment Functions (Wave, Orange Money, Free Money)
// ========================================

let currentOrderId = null;

// Load available payment methods
async function loadPaymentMethods() {
    try {
        const response = await fetch('/api/payment/methods');
        const methods = await response.json();
        return methods;
    } catch (error) {
        console.error('Erreur chargement méthodes paiement:', error);
        return [];
    }
}

// Render payment methods in the checkout page
async function renderPaymentMethods(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;
    
    const methods = await loadPaymentMethods();
    
    container.innerHTML = methods.map(method => `
        <div class="payment-method" data-method="${method.code}">
            <input type="radio" name="paymentMethod" id="payment-${method.code}" value="${method.code}">
            <label for="payment-${method.code}">
                <i class="fas fa-${getPaymentIcon(method.code)}"></i>
                <span>${method.name}</span>
                <small>${method.description}</small>
            </label>
        </div>
    `).join('');
}

// Get icon for payment method
function getPaymentIcon(methodCode) {
    const icons = {
        'WAVE': 'wave-square',
        'ORANGE_MONEY': 'mobile-alt',
        'FREE_MONEY': 'phone-alt',
        'CASH_ON_DELIVERY': 'money-bill-wave'
    };
    return icons[methodCode] || 'credit-card';
}

// Initiate payment
async function initiatePayment(orderId, paymentMethod, phoneNumber = null) {
    try {
        const response = await fetch('/api/payment/initiate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                orderId: orderId,
                paymentMethod: paymentMethod,
                phoneNumber: phoneNumber
            })
        });
        
        const result = await response.json();
        
        if (result.success) {
            // For cash on delivery, redirect to confirmation
            if (paymentMethod === 'CASH_ON_DELIVERY') {
                showNotification(t('payment_success') || 'Commande confirmée!');
                window.location.href = `/confirmation-commande?id=${orderId}`;
            } else {
                // For mobile payments, redirect to checkout URL
                const paymentData = result.payment;
                if (paymentData.checkout_url) {
                    window.location.href = paymentData.checkout_url;
                }
            }
            return result;
        } else {
            showNotification(result.message || t('payment_failed') || 'Erreur de paiement', 'error');
            return null;
        }
    } catch (error) {
        console.error('Erreur paiement:', error);
        showNotification(t('payment_failed') || 'Erreur de paiement', 'error');
        return null;
    }
}

// Show payment modal
function showPaymentModal(orderId, total) {
    currentOrderId = orderId;
    
    const modal = document.createElement('div');
    modal.id = 'payment-modal';
    modal.className = 'payment-modal';
    modal.innerHTML = `
        <div class="payment-modal-content">
            <div class="payment-modal-header">
                <h2>${t('select_payment') || 'Choisir le mode de paiement'}</h2>
                <button class="close-modal" onclick="closePaymentModal()">&times;</button>
            </div>
            <div class="payment-modal-body">
                <div class="payment-total">
                    <span>${t('total') || 'Total'}:</span>
                    <strong>${formatPrice(total)}</strong>
                </div>
                <div class="payment-methods" id="payment-methods-list">
                    <!-- Payment methods will be loaded here -->
                </div>
                <div class="payment-phone" id="payment-phone-section" style="display: none;">
                    <label for="payment-phone">${t('phone') || 'Téléphone'}:</label>
                    <input type="tel" id="payment-phone" placeholder="77 XXX XX XX">
                    <small>Numéro Wave/Orange/Free Money</small>
                </div>
            </div>
            <div class="payment-modal-footer">
                <button class="btn btn-primary" onclick="processPayment()">${t('pay_now') || 'Payer maintenant'}</button>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    
    // Load payment methods
    renderPaymentMethods('payment-methods-list');
    
    // Add event listeners for payment method selection
    document.querySelectorAll('input[name="paymentMethod"]').forEach(input => {
        input.addEventListener('change', function() {
            const phoneSection = document.getElementById('payment-phone-section');
            if (this.value !== 'CASH_ON_DELIVERY') {
                phoneSection.style.display = 'block';
            } else {
                phoneSection.style.display = 'none';
            }
        });
    });
}

// Close payment modal
function closePaymentModal() {
    const modal = document.getElementById('payment-modal');
    if (modal) {
        modal.remove();
    }
}

// Process payment
async function processPayment() {
    const selectedMethod = document.querySelector('input[name="paymentMethod"]:checked');
    
    if (!selectedMethod) {
        showNotification(t('select_payment') || 'Veuillez sélectionner un mode de paiement', 'error');
        return;
    }
    
    const phoneNumber = document.getElementById('payment-phone')?.value;
    const paymentMethod = selectedMethod.value;
    
    // Validate phone for mobile payments
    if (paymentMethod !== 'CASH_ON_DELIVERY' && !phoneNumber) {
        showNotification('Veuillez entrer votre numéro de téléphone', 'error');
        return;
    }
    
    await initiatePayment(currentOrderId, paymentMethod, phoneNumber);
}

// Simulate payment success (for testing)
async function simulatePaymentSuccess(orderId) {
    try {
        const response = await fetch('/api/payment/simulate/success', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ orderId: orderId })
        });
        
        const result = await response.json();
        if (result.success) {
            showNotification(t('payment_success') || 'Paiement confirmé!');
            return true;
        }
        return false;
    } catch (error) {
        console.error('Erreur simulation paiement:', error);
        return false;
    }
}
