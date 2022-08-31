import { Component, ChangeEvent } from "react";
import NewsDataService from "../../services/news.service";
import { Link } from "react-router-dom";
import INewsData from '../../types/news.type';
import IUser from "../../types/user.type";
import AuthService from "../../services/auth.service";
import "bootstrap/dist/css/bootstrap.min.css";
import Swal from "sweetalert2";
import StompClient from "react-stomp-client";

type Props = { };

type State = {
  news: Array<INewsData>,
  current: INewsData | null,
  currentIndex: number,
  searchTitle: string,
  currentUser : IUser | undefined
};

export default class NewsList extends Component<Props, State>{
  constructor(props: Props) {
    super(props);
    this.onChangeSearchTitle = this.onChangeSearchTitle.bind(this);
    this.retrieve = this.retrieve.bind(this);
    this.refreshList = this.refreshList.bind(this);
    this.setActive = this.setActive.bind(this);
    this.removeAll = this.removeAll.bind(this);
    this.searchTitle = this.searchTitle.bind(this);
    this.handleNewsCreate = this.handleNewsCreate.bind(this);

    this.state = {
      news: [],
      current: null,
      currentIndex: -1,
      searchTitle: "",
      currentUser : undefined
    };
  }

  handleNewsCreate(webSocketMessage:any) {
    const data = JSON.parse(webSocketMessage.body);
    this.setState({ news:[...this.state.news,data]});
  }


  componentDidMount() {
    this.retrieve();
    const user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        currentUser: user
      });
    }
  }

  onChangeSearchTitle(e: ChangeEvent<HTMLInputElement>) {
    const searchTitle = e.target.value;

    this.setState({
      searchTitle: searchTitle
    });
  }

  retrieve() {
    NewsDataService.getAll()
      .then((response: any) => {
        this.setState({
          news: response.data
        });
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  refreshList() {
    this.retrieve();
    this.setState({
      current: null,
      currentIndex: -1
    });
  }

  setActive(news: INewsData, index: number) {
    this.setState({
      current: news,
      currentIndex: index
    });
  }

  removeAll() {
    NewsDataService.deleteAll()
      .then((response: any) => {
        console.log(response.data);
        this.refreshList();
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  searchTitle() {
    this.setState({
      current: null,
      currentIndex: -1
    });

    NewsDataService.findByTitle(this.state.searchTitle)
      .then((response: any) => {
        this.setState({
          news: response.data
        });
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  popup(news:INewsData){
    Swal.fire({
      title: news.title,
      text: news.description,
      imageUrl: 'https://unsplash.it/400/200',
      imageWidth: 400,
      imageHeight: 200,
      imageAlt: 'Custom image',
    })
  }


  render() {

    const { currentUser,searchTitle, news, current, currentIndex } = this.state;

    return(
        <StompClient
            endpoint="ws://localhost:8080/websocket"
            topic="topic/news.create"
            onMessage={this.handleNewsCreate}
        >
          <div className="list row">

            <div className="col-md-8">
              <div className="input-group mb-3">
                <input
                  type="text"
                  className="form-control"
                  placeholder="Haber Arama"
                  value={searchTitle}
                  onChange={this.onChangeSearchTitle}
                />
                <div className="input-group-append">
                  <button
                    className="btn btn-outline-secondary"
                    type="button"
                    onClick={this.searchTitle}
                  >
                    Ara
                  </button>
                </div>
              </div>
            </div>
            <div className="col-md-6">
              <h4>Haber Listesi</h4>

              <ul className="list-group">
                {news &&
                  news.map((news: INewsData, index: number) => (
                    <li
                      className={
                        "list-group-item " +
                        (index === currentIndex ? "active" : "")
                      }
                      onClick={() => currentUser ? (this.setActive(news, index)):(this.popup(news))}
                      key={index}
                    >
                      {news.title}
                    </li>
                  ))}
              </ul>

              {currentUser ? (news ? (<button
                  className="m-3 btn btn-sm btn-danger"
                  onClick={this.removeAll}
              >
                Hepsini sil
              </button>):(<div/>)):(<div/>)}


            </div>
            <div className="col-md-6">
              {current ? (
                  <div>
                    <h4>Haber</h4>
                    <div>
                      <label>
                        <strong>Başlık:</strong>
                      </label>{" "}
                      {current.title}
                    </div>
                    <div>
                      <label>
                        <strong>Açıklama:</strong>
                      </label>{" "}
                      {current.description}
                    </div>
                    <div>
                      <label>
                        <strong>Durumu:</strong>
                      </label>{" "}
                      {current.published ? "Yayında" : "Yayında Değil"}
                    </div>

                    <Link
                        to={"/news/" + current.id}
                        className="badge badge-warning"
                    >
                      Düzenle
                    </Link>
                  </div>
              ) : (
                <div>
                  {news ? (
                  <div>
                    <br/>
                    <p>Lütfen habere tıklayın...</p>
                  </div>):(<div/>)}

                </div>
              )}
            </div>
          </div>
        </StompClient>
    );
  }
}
