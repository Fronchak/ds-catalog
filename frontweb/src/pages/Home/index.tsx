import ButtonIcon from '../../components/ButtonIcon';
import { ReactComponent as MainImage } from '../../assets/imgs/main-image.svg';
import Navbar from '../../components/Navbar';
import './styles.css';

const Home = () => {
  return (
    <>
      <Navbar />
      <div className="home-container">
        <div className="home-card">
          <div className="home-content-container">
            <h1>Conheça o melhor catálogo de produtos</h1>
            <p>Ajudaremos você a encontrar os melhores produtos disponíveis no mercado.</p>
            <div className="button-icon-container">
              <ButtonIcon />
            </div>
          </div>
          <div className="home-image-container">
            <MainImage />
          </div>
        </div>
      </div>
    </>
  );
}

export default Home;
