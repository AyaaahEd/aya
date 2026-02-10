const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function fetchProducts() {
    const res = await fetch(`${API_BASE_URL}/products`, { cache: 'no-store' });
    if (!res.ok) {
        throw new Error('Failed to fetch products');
    }
    return res.json();
}

export async function fetchProductById(id: string) {
    const res = await fetch(`${API_BASE_URL}/products/${id}`, { cache: 'no-store' });
    if (!res.ok) {
        // If 404, we might return null or throw
        if (res.status === 404) return null;
        throw new Error('Failed to fetch product');
    }
    return res.json();
}

export async function createOrder(orderData: any) {
    const res = await fetch(`${API_BASE_URL}/orders`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(orderData),
    });
    if (!res.ok) {
        throw new Error('Failed to create order');
    }
    return res.json();
}

export async function registerUser(userData: any) {
    const res = await fetch(`${API_BASE_URL}/users/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
    });
    if (!res.ok) {
        const error = await res.text();
        throw new Error(error || 'Failed to register');
    }
    return res.text(); // Register returns text message
}

export async function loginUser(credentials: any) {
    const res = await fetch(`${API_BASE_URL}/users/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
    });
    if (!res.ok) {
        const error = await res.text();
        throw new Error(error || 'Failed to login');
    }
    return res.json(); // Login returns User object
}

export async function fetchOrders(userId: string) {
    const res = await fetch(`${API_BASE_URL}/orders/user/${userId}`);
    if (!res.ok) {
        throw new Error('Failed to fetch orders');
    }
    return res.json();
}

export async function fetchAllOrders() {
    const res = await fetch(`${API_BASE_URL}/orders`);
    if (!res.ok) {
        throw new Error('Failed to fetch all orders');
    }
    return res.json();
}

export async function updateOrderStatus(orderId: string, status: string) {
    const res = await fetch(`${API_BASE_URL}/orders/${orderId}/status`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'text/plain',
        },
        body: status,
    });
    if (!res.ok) {
        throw new Error('Failed to update order status');
    }
    return res.json();
}
