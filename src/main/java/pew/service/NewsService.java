
package pew.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import pew.domain.NewsObject;
import pew.repository.AuthorRepository;
import pew.repository.CategoryRepository;
import pew.repository.NewRepository;

@Service
public class NewsService {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @Transactional
    public List<NewsObject> getPopularNews(){
        List<NewsObject> news = newRepo.findAll();
        Collections.sort(news,
                 new Comparator<NewsObject>()
                 {
                     public int compare(NewsObject o2,
                                        NewsObject o1)
                     {
                         if (o1.getWeeklyViews()==
                                 o2.getWeeklyViews())
                         {
                             return 0;
                         }
                         else if (o1.getWeeklyViews() <
                                      o2.getWeeklyViews())
                         {
                             return -1;
                         }
                         return 1;
                     }
                 });
        if(news.size() > 5){
            return news.subList(0, 5);
        }
        return news;
    }
    
    @Transactional
    public List<NewsObject> getAllPopularNews(){
        List<NewsObject> news = newRepo.findAll();
        Collections.sort(news,
                 new Comparator<NewsObject>()
                 {
                     public int compare(NewsObject o2,
                                        NewsObject o1)
                     {
                         if (o1.getWeeklyViews()==
                                 o2.getWeeklyViews())
                         {
                             return 0;
                         }
                         else if (o1.getWeeklyViews() <
                                      o2.getWeeklyViews())
                         {
                             return -1;
                         }
                         return 1;
                     }
                 });
        return news;
    }
    
    @Transactional
    public HttpHeaders getHeaders(NewsObject n){
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(n.getContentType()));
        headers.setContentLength(n.getSize());
        return headers;
    }
}
