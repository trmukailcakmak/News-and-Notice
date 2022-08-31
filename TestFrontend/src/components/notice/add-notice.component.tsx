import { Component, ChangeEvent } from "react";
import NoticeDataService from "../../services/notice.service";
import INoticeData from '../../types/notice.type';
import Swal from "sweetalert2";
import "../../App.css"

type Props = {};

type State = INoticeData & {
  submitted: boolean
};

export default class AddNotice extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.onChangeTitle = this.onChangeTitle.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    this.save = this.save.bind(this);
    this.notice = this.notice.bind(this);

    this.state = {
      id: null,
      title: "",
      description: "",
      published: false,
      submitted: false
    };
  }

  onChangeTitle(e: ChangeEvent<HTMLInputElement>) {
    this.setState({
      title: e.target.value
    });
  }

  onChangeDescription(e: ChangeEvent<HTMLInputElement>) {
    this.setState({
      description: e.target.value
    });
  }

  save() {
    if (this.state.title==""&&this.state.description==""){
      Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Başlık ve açıklama alanı boş olamaz!'
    })
    }else if(this.state.title==""){
      Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Başlık alanı boş olamaz!'
    })}else if (this.state.description==""){
      Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Açıklama alanı boş olamaz!'
    })}else {
    const data: INoticeData = {
      title: this.state.title,
      description: this.state.description
    };

    NoticeDataService.create(data)
      .then((response: any) => {
        this.setState({
          id: response.data.id,
          title: response.data.title,
          description: response.data.description,
          published: response.data.published,
          submitted: true
        });
        if (this.state.submitted==true){
          Swal.fire({
            icon: 'success',
            title: 'Duyuru Eklendi',
            text: 'Tekrar eklemek için ok basın'
          })
        }
        console.log(response.data);
        this.notice();
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }}

  notice() {
    this.setState({
      id: null,
      title: "",
      description: "",
      published: false,
      submitted: false
    });
  }

  render() {
    const { submitted, title, description } = this.state;

    return (
      <div id="card-container" className="submit-form">
          <div className="card card-container">
            <strong><h3 className="ml-auto">Duyuru Ekle</h3></strong>
            <div className="form-group">
              <label htmlFor="title">Başlık</label>
              <input
                type="text"
                className="form-control"
                id="title"
                required
                value={title}
                onChange={this.onChangeTitle}
                name="title"
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">Açıklama</label>
              <input
                type="text"
                className="form-control"
                id="description"
                required
                value={description}
                onChange={this.onChangeDescription}
                name="description"
              />
            </div><br/><br/>

            <button onClick={this.save} className="btn btn-success">
              Ekle
            </button>
          </div>
      </div>
    );
  }
}
