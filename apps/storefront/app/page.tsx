import { fetchProducts } from "../lib/api";
import { Button } from "@repo/ui/button";

export default async function Home() {
  let products = [];
  try {
    products = await fetchProducts();
  } catch (e) {
    console.error("Failed to fetch products", e);
  }

  return (
    <div className="w-full">

      <div className="w-full max-w-5xl">
        <h2 className="text-2xl font-semibold mb-6">Featured Products</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {products.length === 0 ? (
            <div className="col-span-3 text-center border-dashed border-2 border-gray-300 p-10 rounded-lg">
              <p className="text-lg text-gray-600">No products available.</p>
              <p className="text-sm text-gray-400 mt-2">
                (If you are running this locally without the backend, this is expected.)
              </p>
            </div>
          ) : (
            products.map((p: any) => (
              <div key={p.id} className="border border-gray-200 p-6 rounded-lg shadow-sm hover:shadow-md transition bg-white">
                <h3 className="text-xl font-bold mb-2">{p.name}</h3>
                <p className="text-gray-600 mb-4 h-12 overflow-hidden">{p.description}</p>
                <div className="flex justify-between items-center">
                  <span className="text-lg font-bold text-green-600">${p.price}</span>
                  <Button appName="storefront" className="bg-black text-white px-3 py-1 rounded text-sm">
                    View
                  </Button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
