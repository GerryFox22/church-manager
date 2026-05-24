export interface News {
  id: number;
  title: string;
  content: string;
  imageUrl: string | null;
  createdAt: string;
}

export interface NewsFormValue {
  title: string;
  content: string;
  image?: File | null;
}
