@extends('layouts.app')

@section('content')
<link href="{{ asset('css/app2.css') }}" rel="stylesheet">
<link href="{{ asset('css/commentcss.css') }}" rel="stylesheet">
<style>
    .nav-3{
        border-bottom: 3px solid white;
    }
</style>
<div class="container">
    <div class="page-header">
        <ul id="detail" class="tab tab_item4">
            <li class="free_btn" style="cursor:pointer;" onclick="free(this)"><a>자유게시판</a></li>
            <li class="sugesstion_btn" style="cursor:pointer;" onclick="sugesstion(this)"><a>건의사항</a></li>
            <li class="modify_btn" style="cursor:pointer;" onclick="modify(this)"><a>데이터 수정 요청</a></li>
        </ul>
    </div>
    <hr/>
    <ul class="list_ul">
        @forelse($qnaArticles as $article)
            @include('qnaArticles.partial.qnaArticle')
            <hr>
    @empty
        <p>글이 없습니다.</p>
        @endforelse
    </ul>

    @if($qnaArticles->count())
    <!--원소가 있을 때만 페이지 이동 링크를 표시 -->
        <div class="text-center">
            <!-- {!! $qnaArticles->render() !!} -->
            {!! $qnaArticles->appends(Request::except('page'))->render() !!}
        </div>
    @endif
    <button type="button" class="btn btn-primary" onclick="location.href='{{ route('qnaArticles.create')}}'">ADD</button>
</div>
@section('script')
    <script>
        $.ajaxSetup({
            headers: {
                'X-CSRF-TOKEN' : $('meta[name="csrf-token"]').attr('content')
            }
        });
        $.ajax({
            type:"GET",
            url:"qnaArticles",
        })
        .then((qnaArticles) =>{
            console.log(qnaArticles)
        })

        function free(e){
            var free_btn = $(e).attr('class');
            var parent = $(e).closest('.container');
            var list_ul = $(parent).children('.list_ul')
            
            if($(list_ul).find('.sugesstion')){
                var hr = $(list_ul).find('.sugesstion')
                $(list_ul).find('.sugesstion').hide()
                $(hr).next().hide()
            }
            if($(list_ul).find('.modify')){
                var hr = $(list_ul).find('.modify')
                $(list_ul).find('.modify').hide()
                $(hr).next().hide()
            }

            var hr = $(list_ul).find('.free')
            $(list_ul).find('.free').show()
            $(hr).next().show()
        }

        function sugesstion(e){
            var free_btn = $(e).attr('class');
            var parent = $(e).closest('.container');
            var list_ul = $(parent).children('.list_ul')
            
            if($(list_ul).find('.free')){
                var hr = $(list_ul).find('.free')
                $(list_ul).find('.free').hide()
                $(hr).next().hide()
            }
            if($(list_ul).find('.modify')){
                var hr = $(list_ul).find('.modify')
                $(list_ul).find('.modify').hide()
                $(hr).next().hide()
            }

            var hr = $(list_ul).find('.sugesstion')
            $(list_ul).find('.sugesstion').show()
            $(hr).next().show()
        }

        function modify(e){
            var free_btn = $(e).attr('class');
            var parent = $(e).closest('.container');
            var list_ul = $(parent).children('.list_ul')
            
            if($(list_ul).find('.free')){
                var hr = $(list_ul).find('.free')
                $(list_ul).find('.free').hide()
                $(hr).next().hide()
            }
            if($(list_ul).find('.sugesstion')){
                var hr = $(list_ul).find('.sugesstion')
                $(list_ul).find('.sugesstion').hide()
                $(hr).next().hide()
            }

            var hr = $(list_ul).find('.modify')
            $(list_ul).find('.modify').show()
            $(hr).next().show()
        }
    </script>

@stop