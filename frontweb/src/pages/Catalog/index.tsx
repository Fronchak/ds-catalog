import ProductCard from '../../components/ProductCard';
import Navbar from '../../components/Navbar';


const Catalog = () => {

  return (
    <>
      <Navbar />
      <h1>Tela de catalogo</h1>
      <div className="container my-4">
        <div className="row">
          <div className="col-12 col-sm-6 col-md-4 col-lg-3">
            <ProductCard />
          </div>
          <div className="col-12 col-sm-6 col-md-4 col-lg-3">
            <ProductCard />
          </div>
          <div className="col-12 col-sm-6 col-md-4 col-lg-3">
            <ProductCard />
          </div>
          <div className="col-12 col-sm-6 col-md-4 col-lg-3">
            <ProductCard />
          </div>
        </div>
      </div>
    </>
  );
}

export default Catalog;
