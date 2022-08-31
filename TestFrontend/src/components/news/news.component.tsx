import { Component, ChangeEvent } from "react";
import { RouteComponentProps } from 'react-router-dom';

import NewsDataService from "../../services/news.service";
import INewsData from "../../types/news.type";
import "../../App.css"

interface RouterProps { // type for `match.params`
  id: string; // must be type `string` since value comes from the URL
}

type Props = RouteComponentProps<RouterProps>;

type State = {
  current: INewsData;
  message: string;
}

export default class NewsComponent extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.onChangeTitle = this.onChangeTitle.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    this.getNews = this.getNews.bind(this);
    this.updatePublished = this.updatePublished.bind(this);
    this.update = this.update.bind(this);
    this.delete = this.delete.bind(this);

    this.state = {
      current: {
        id: null,
        title: "",
        description: "",
        published: false,
      },
      message: "",
    };
  }

  componentDidMount() {
    this.getNews(this.props.match.params.id);
  }

  onChangeTitle(e: ChangeEvent<HTMLInputElement>) {
    const title = e.target.value;

    this.setState(function (prevState) {
      return {
        current: {
          ...prevState.current,
          title: title,
        },
      };
    });
  }

  onChangeDescription(e: ChangeEvent<HTMLInputElement>) {
    const description = e.target.value;

    this.setState((prevState) => ({
      current: {
        ...prevState.current,
        description: description,
      },
    }));
  }

  getNews(id: string) {
    NewsDataService.get(id)
      .then((response: any) => {
        this.setState({
          current: response.data,
        });
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  updatePublished(status: boolean) {
    const data: INewsData = {
      id: this.state.current.id,
      title: this.state.current.title,
      description: this.state.current.description,
      published: status,
    };

    NewsDataService.update(data, this.state.current.id)
      .then((response: any) => {
        this.setState((prevState) => ({
          current: {
            ...prevState.current,
            published: status,
          },
          message: "Durum başarıyla güncelendi!"
        }));
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  update() {
    NewsDataService.update(
      this.state.current,
      this.state.current.id
    )
      .then((response: any) => {
        console.log(response.data);
        this.setState({
          message: "Haber başarıyla güncelendi!",
        });
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  delete() {
    NewsDataService.delete(this.state.current.id)
      .then((response: any) => {
        console.log(response.data);
        this.props.history.push("/newsList");
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  render() {
    const { current } = this.state;

    return (
      <div id="card-container-1" className="card card-container">
        {current ? (
          <div className="edit-form">
            <h4>Haber Düzenleme</h4>
            <form>
              <div className="form-group">
                <label htmlFor="title">Başlık</label>
                <input
                  type="text"
                  className="form-control"
                  id="title"
                  value={current.title}
                  onChange={this.onChangeTitle}
                />
              </div>
              <div className="form-group">
                <label htmlFor="description">Açıklama</label>
                <input
                  type="text"
                  className="form-control"
                  id="description"
                  value={current.description}
                  onChange={this.onChangeDescription}
                />
              </div>

              <div className="form-group">
                <label>
                  <strong>Durumu:</strong>
                </label>
                {current.published ? "Yayında" : "Yayında değil"}
              </div>
            </form>

            {current.published ? (
              <button
                className="badge badge-primary mr-2"
                onClick={() => this.updatePublished(false)}
              >
                Yayından Çıkar
              </button>
            ) : (
              <button
                className="badge badge-primary mr-2"
                onClick={() => this.updatePublished(true)}
              >
                Yayınla
              </button>
            )}

            <button
              className="badge badge-danger mr-2"
              onClick={this.delete}
            >
              Sil
            </button>

            <button
              type="submit"
              className="badge badge-success"
              onClick={this.update}
            >
              Güncele
            </button>
            <p>{this.state.message}</p>
          </div>
        ) : (
          <div>
            <br />
            <p>Lütfen habere tıklayın...</p>
          </div>
        )}
      </div>
    );
  }
}
