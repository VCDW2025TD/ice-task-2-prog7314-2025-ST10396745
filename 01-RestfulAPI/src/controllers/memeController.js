import Meme from '../models/Meme.js'

export async function createMeme (req, res, next) {
  try {
    const { userId, title, imageUrl, tags } = req.body
    if (!userId || !title || !imageUrl) {
      return res.status(400).json({ error: 'userId, title, and imageUrl are required' })
    }
    const meme = await Meme.create({
      userId, title, imageUrl, tags: Array.isArray(tags) ? tags : []
    })
    res.status(201).json(meme)
  } catch (err) { next(err) }
}

export async function getMemes (req, res, next) {
  try {
    const { userId } = req.query
    const limit = Math.min(parseInt(req.query.limit) || 50, 100)
    const skip = parseInt(req.query.skip) || 0
    const query = {}
    if (userId) query.userId = userId
    const [items, total] = await Promise.all([
      Meme.find(query).sort({ createdAt: -1 }).skip(skip).limit(limit),
      Meme.countDocuments(query)
    ])
    res.json({ total, count: items.length, items })
  } catch (err) { next(err) }
}
